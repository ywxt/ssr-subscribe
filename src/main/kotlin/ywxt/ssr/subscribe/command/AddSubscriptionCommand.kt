package ywxt.ssr.subscribe.command

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import picocli.CommandLine
import ywxt.ssr.subscribe.async.http.AsyncClient
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.config.SubscriptionConfig
import ywxt.ssr.subscribe.exception.HttpException
import ywxt.ssr.subscribe.exception.ParseException
import ywxt.ssr.subscribe.ssr.SsrUrl
import java.net.MalformedURLException
import java.net.URL
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.console.printGroup

@CommandLine.Command(name = "add")
class AddSubscriptionCommand : Runnable {
    @CommandLine.Parameters
    private lateinit var url: String
    override fun run() {
        try {
            val httpUrl = URL(url)
            runBlocking {
                //10s 超时
                val urls = withTimeout(10000) {
                    AsyncClient().requestSsrUrls(httpUrl)
                }
                val config = ConfigFile.load()
                config.subscriptionConfig.add(SubscriptionConfig(
                    url = url,
                    servers = urls.map { ServerConfig.from(it, config.defaultLocalConfig) }
                ))
            }
        } catch (e: MalformedURLException) {
            eprintln("URL不正确：${url}")
        } catch (e: ParseException) {
            eprintln("解析错误：${e.message}")
        } catch (e: HttpException) {
            eprintln("HTTP错误：${e.message}")
        } catch (e: TimeoutCancellationException) {
            eprintln("网络超时")
        }
    }

    companion object {
        private fun showDetail(url: String, urls: List<SsrUrl>) {
            println("订阅地址：${url}")
            println("服务器：")
            val prettyServers = urls
                .groupBy { it.urlParams.group }
                .asSequence()
                .map { group ->
                    val key = if (group.key.isBlank()) "未命名" else group.key
                    val value = group.value
                        .map { url ->
                            if (url.urlParams.remarks.isBlank()) "${url.urlBase.server}:${url.urlBase.port}"
                            else "${url.urlParams.remarks} (${url.urlBase.server}:${url.urlBase.port})"
                        }
                    Pair(key, value)
                }.associateBy({ it.first }, { it.second })
            prettyServers.forEach {
                printGroup(it.key, it.value)
            }
        }
    }
}