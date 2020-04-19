package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.util.config.groups
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.console.printGroups
import java.lang.Exception

class ServerCommand : CliktCommand(name = "server",help = "查看订阅服务器") {
    val server: String? by argument(help = "服务器（组）序号")
    val group: Boolean by option("--group", "-g",help = "查看组").flag(default = false)
    val file: String? by option("--file", "-f",help = "配置文件位置")
    override fun run() = runBlocking {
        val config = try {
            loadConfigFile(file)
        } catch (e: Exception) {
            handleLoadJsonConfigException(e)
            return@runBlocking
        }
        if (server.isNullOrBlank()) {
            printServers(config.servers)
            return@runBlocking
        }
        val groups = config.servers
            .groups()
            .entries
            .toList()
        if (!group) {
            try {
                val indexes = server!!.split('-').map { it.toInt() }
                val serverConfig = groups[indexes[0]].value[indexes[1]]
                printServer(serverConfig)
            } catch (_: Exception) {
                eprintln("输入序号不正确")
            } finally {
                return@runBlocking
            }
        }
        try {
            val index = server!!.toInt()
            val serverConfigs = groups[index].value
            printServers(serverConfigs)
        } catch (_: Exception) {
            eprintln("输入序号不正确")
        }
    }

    private fun printServer(serverConfig: ServerConfig) = println(serverConfig.toString())

    private fun printServers(servers: Iterable<ServerConfig>) = printGroups(servers.groups())
}