package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import ywxt.ssr.subscribe.async.http.AsyncClient
import ywxt.ssr.subscribe.config.LocalConfig
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.exception.HttpException
import ywxt.ssr.subscribe.exception.ParseException
import ywxt.ssr.subscribe.util.config.groups
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.console.*
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

class AddSourceCommand : CliktCommand(name = "add",help = "添加订阅源") {
    val url: String by argument(help = "订阅地址")
    val file: String? by option("--file","-f",help = "配置文件地址")
    override fun run() {
        try {
            val httpUrl = URL(url)
            runBlocking {
                //10s 超时
                val ssrUrls = withTimeout(10000) {
                    AsyncClient().requestSsrUrls(httpUrl)
                }
                showDetail(url, ssrUrls.map { ServerConfig.from(it, url, LocalConfig.DEFAULT_LOCAL_CONFIG) })
                val confirmed = confirm("是否添加到订阅？")
                if (!confirmed) return@runBlocking
                val config = try {
                    loadConfigFile(file)
                }catch (e:Exception){
                    handleLoadJsonConfigException(e)
                    return@runBlocking
                }
                ssrUrls.forEach {
                    config.servers.add(ServerConfig.from(it, url, config.defaultLocalConfig))
                }
                config.sources.add(url)
                config.save()
                sprintln("添加成功")
            }
        } catch (_: MalformedURLException) {
            eprintln("URL不正确：${url}")
        } catch (e: ParseException) {
            eprintln("解析错误：${e.localizedMessage}")
        } catch (e: HttpException) {
            eprintln("HTTP错误：${e.localizedMessage}")
        } catch (_: TimeoutCancellationException) {
            eprintln("网络超时")
        }catch (e:IOException){
            eprintln("IO异常:${e.localizedMessage}")
        }
    }

    private fun showDetail(url: String, urls: Iterable<ServerConfig>) {
        println("订阅地址：${url}")
        println("服务器：")
        // 按组分类
        val prettyServers = urls.groups()
        // 打印订阅的服务器
        printGroups(prettyServers)
    }
}