package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.console.printSources
import java.lang.Exception

class SourceCommand:CliktCommand(name = "source",help = "查看订阅源") {
    val file:String? by option("--file","-f",help = "配置文件地址")
    override fun run() = runBlocking {
        val config = try {
            loadConfigFile(file)
        }catch (e:Exception){
            handleLoadJsonConfigException(e)
            return@runBlocking
        }
        printSources(config.sources)
    }
}