package ywxt.ssr.subscribe.command

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
import java.lang.Exception

class ServerCommand : CliktCommand(name = "server") {
    val server: String? by argument()
    val group: Boolean by option("--group", "-g").flag(default = false)
    override fun run() = runBlocking {
        val config = ConfigFile.load()
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