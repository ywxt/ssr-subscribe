package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ywxt.ssr.subscribe.async.http.AsyncClient
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.exception.HttpException
import ywxt.ssr.subscribe.exception.ParseException
import ywxt.ssr.subscribe.ssr.SsrUrl
import ywxt.ssr.subscribe.util.console.confirm
import java.net.MalformedURLException
import java.net.URL
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.console.printGroups
import ywxt.ssr.subscribe.util.ssrurl.groups

class AddSubscriptionCommand : CliktCommand(name = "add") {
    val url: String by argument()
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
                ssrUrls.forEach {
                    config.servers.add(ServerConfig.from(it, url, config.defaultLocalConfig))
                }
                config.sources.add(url)
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
            val prettyServers = urls.groups()
            // 打印订阅的服务器
            printGroups(prettyServers)
        }
    }
}