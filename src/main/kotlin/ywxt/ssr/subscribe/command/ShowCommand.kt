package ywxt.ssr.subscribe.command

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.config.SsrServerConfig
import ywxt.ssr.subscribe.util.console.eprintln
import java.io.IOException

class ShowCommand : CliktCommand(name = "show") {
    val path: String? by argument()
    override fun run() = runBlocking {
        try {
            val config = if (path.isNullOrBlank()) {
                SsrServerConfig.load()
            } else {
                SsrServerConfig.load(path!!)
            }
            println(config.toString())
        }catch (_:NoSuchFileException){
            eprintln("未找到文件:$path")
        }catch (_: JsonProcessingException){
          eprintln("无效的配置文件:$path")
        } catch (e:IOException){
            eprintln("IO异常:${e.localizedMessage}")
        }
    }

}