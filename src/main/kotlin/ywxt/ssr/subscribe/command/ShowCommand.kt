package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.config.loadSsrServerConfig
import java.lang.Exception

class ShowCommand : CliktCommand(name = "show", help = "查看默认服务器详细信息") {
    val path: String by argument().default("config.json")
    override fun run() = runBlocking {
        val config = try {
            loadSsrServerConfig(path)
        } catch (e: Exception) {
            handleLoadJsonConfigException(e)
            return@runBlocking
        }
        println(config.toString())

    }

}