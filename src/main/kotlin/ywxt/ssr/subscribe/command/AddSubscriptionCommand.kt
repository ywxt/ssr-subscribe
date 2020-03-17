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
import ywxt.ssr.subscribe.util.console.confirm
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
                val ssrUrls = withTimeout(10000) {
                    AsyncClient().requestSsrUrls(httpUrl)
                }
                showDetail(url, ssrUrls)
                val confirmed = confirm("是否添加到订阅？")
                if (!confirmed) return@runBlocking
                val config = ConfigFile.load()
                config.subscriptionConfig.add(SubscriptionConfig(
                    url = url,
                    servers = ssrUrls.map { ServerConfig.from(it, config.defaultLocalConfig) }
                ))
                config.save()

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
            // 按组分类
            val prettyServers = urls
                .groupBy { it.urlParams.group }
                .asSequence()
                .map { group ->
                    val key = if (group.key.isBlank()) "未命名" else group.key
                    val value = group.value
                        .asSequence()
                        .map { ssrUrl ->
                            if (ssrUrl.urlParams.remarks.isBlank()) "${ssrUrl.urlBase.server}:${ssrUrl.urlBase.port}"
                            else "${ssrUrl.urlParams.remarks} (${ssrUrl.urlBase.server}:${ssrUrl.urlBase.port})"
                        }
                    Pair(key, value)
                }.associateBy({ it.first }, { it.second })
            // 打印订阅的服务器
            prettyServers.forEach {
                printGroup(it.key, it.value)
            }
        }
    }
}