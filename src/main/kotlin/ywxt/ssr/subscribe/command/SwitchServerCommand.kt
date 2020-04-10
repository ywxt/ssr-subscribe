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
import ywxt.ssr.subscribe.util.config.groups
import ywxt.ssr.subscribe.util.console.eprintln
import java.io.IOException
import java.lang.Exception


class SwitchServerCommand : CliktCommand(name = "switch") {
    val server: String by argument()
    val path :String by option("--path","-p").default("config.json")
    override fun run() = runBlocking {
        val config = ConfigFile.load()
        try {
            val indexes = server.split('-').map { it.toInt() }
            val serverConfig = config.servers
                .groups()
                .entries
                .elementAt(indexes[0])
                .value
                .elementAt(indexes[1])
            val ssrServerConfig = JSON_MAPPER.writeValueAsBytes(serverConfig)
            AsyncFile(path).use { it.write(ssrServerConfig)}
        }catch (e:Exception){
            when (e){
                is IndexOutOfBoundsException, is NumberFormatException -> eprintln("输入序号不正确")
                is JsonProcessingException -> eprintln("服务器配置不正确")
                is IOException, is IllegalStateException -> eprintln("无法写入配置文件:${path}")
                else -> throw e
            }
        }
    }

}