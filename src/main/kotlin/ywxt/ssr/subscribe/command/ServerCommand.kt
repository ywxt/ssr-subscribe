package ywxt.ssr.subscribe.command

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.util.config.groups
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.console.printGroups
import java.io.IOException
import java.lang.Exception

class ServerCommand : CliktCommand(name = "server") {
    val server: String? by argument()
    val group: Boolean by option("--group", "-g").flag(default = false)
    val file: String? by option("--file", "-f")
    override fun run() = runBlocking {
        val config = try {
            if (file.isNullOrBlank()) {
                ConfigFile.load()
            } else {
                ConfigFile.load(file!!)
            }
        } catch (_: JsonProcessingException) {
            eprintln("配置文件无法解析")
            return@runBlocking
        } catch (_: NoSuchFileException) {
            eprintln("未找到配置文件")
            return@runBlocking
        } catch (e: IOException) {
            eprintln("IO异常:${e.localizedMessage}")
            return@runBlocking
        }
        if (server.isNullOrBlank()) {
            printServers(config.servers)
            return@runBlocking
        }
        if (!group) {
            try {
                val indexes = server!!.split('-').map { it.toInt() }
                val serverConfig = config.servers
                    .groups()
                    .entries
                    .elementAt(indexes[0])
                    .value
                    .elementAt(indexes[1])
                printServer(serverConfig)
            } catch (_: Exception) {
                eprintln("输入序号不正确")
            } finally {
                return@runBlocking
            }
        }
        try {
            val indexes = server!!.toInt()
            val serverConfigs = config.servers
                .groups()
                .entries
                .elementAt(indexes)
                .value
            printServers(serverConfigs)
        } catch (_: Exception) {
            eprintln("输入序号不正确")
        }
    }

    private fun printServer(serverConfig: ServerConfig) = println(serverConfig.toString())

    private fun printServers(servers: Iterable<ServerConfig>) = printGroups(servers.groups())
}