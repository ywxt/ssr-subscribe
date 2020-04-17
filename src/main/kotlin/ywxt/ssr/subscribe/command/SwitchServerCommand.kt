package ywxt.ssr.subscribe.command

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.async.file.AsyncFile
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.json.JSON_MAPPER
import ywxt.ssr.subscribe.ssr.SsrServerConfigConvert
import ywxt.ssr.subscribe.util.config.groups
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.console.eprintln
import java.io.IOException
import java.lang.Exception


class SwitchServerCommand : CliktCommand(name = "switch", help = "切换默认服务器") {
    val server: String by argument(help = "服务器序号")
    val path: String by option("--path", "-p",help = "SSR配置文件路径").default("config.json")
    val file:String? by option("--path","-p",help = "配置文件位置")
    override fun run() = runBlocking {
        val config = try {
            loadConfigFile(file)
        }catch (e:Exception){
            handleLoadJsonConfigException(e)
            return@runBlocking
        }
        try {
            val indexes = server.split('-').map { it.toInt() }
            val serverConfig = config.servers
                .groups()
                .entries
                .elementAt(indexes[0])
                .value
                .elementAt(indexes[1])
            val ssrServerConfig = JSON_MAPPER
                .writeValueAsBytes(SsrServerConfigConvert.from(serverConfig))
            AsyncFile(path).use { it.write(ssrServerConfig) }
        } catch (e: Exception) {
            when (e) {
                is IndexOutOfBoundsException, is NumberFormatException -> eprintln("输入序号不正确")
                is JsonProcessingException -> eprintln("服务器配置不正确")
                is IOException, is IllegalStateException -> eprintln("无法写入配置文件:${path}")
                else -> eprintln(e.localizedMessage)
            }
        }
    }

}