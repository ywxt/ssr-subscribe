package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.*
import ywxt.ssr.subscribe.async.http.AsyncClient
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.exception.HttpException
import ywxt.ssr.subscribe.exception.ParseException
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.console.eprintln
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.net.MalformedURLException
import java.net.URL

class UpdateCommand : CliktCommand(name = "update", help = "更新订阅源") {
    private val client = AsyncClient()

    val file: String? by option("--file", "-f")
    val sourceIndexs: List<String> by argument("SOURCE_INDEX").multiple(false)
    override fun run() = runBlocking {
        val config = try {
            loadConfigFile(file)
        } catch (e: Exception) {
            handleLoadJsonConfigException(e)
            return@runBlocking
        }
        val sources = if (sourceIndexs.isEmpty()) config.sources else {
            try {
                val tmpSources = config.sources.toList()
                sourceIndexs.asSequence()
                    .filter { it.isNotBlank() }
                    .map { tmpSources[it.toInt()] }
                    .asIterable()
            } catch (e: NumberFormatException) {
                eprintln("输入源序号不正确")
                return@runBlocking
            } catch (e: IndexOutOfBoundsException) {
                eprintln("输入源序号不正确")
                return@runBlocking
            }
        }
        try {
            updateConfig(config, sources)
        } catch (e: MalformedURLException) {
            eprintln("URL不正确:${e.localizedMessage}")
        } catch (e: ParseException) {
            eprintln("解析错误：${e.localizedMessage}")
        } catch (e: HttpException) {
            eprintln("HTTP错误：${e.localizedMessage}")
        } catch (_: TimeoutCancellationException) {
            eprintln("网络超时")
        } catch (e: IOException) {
            eprintln("IO异常:${e.localizedMessage}")
        }
    }

    /**
     * 获得订阅源的服务器
     */
    private suspend fun update(url: String) = withTimeout(10000) {
        client.requestSsrUrls(URL(url))
    }


    private suspend fun updateConfig(config: ConfigFile, sources: Iterable<String>) = coroutineScope {
        val tmpSources = sources.toHashSet()
        config.servers.removeIf { it.source in tmpSources }
        val servers = sources
            .map { async { Pair(it, update(it)) } }
            .awaitAll() //async封装，awaitAll并发请求
            .asSequence()
            .flatMap { pair -> //数组展平
                pair.second.asSequence().map { ServerConfig.from(it, pair.first, config.defaultLocalConfig) }
            }
        config.servers.addAll(servers)
    }
}