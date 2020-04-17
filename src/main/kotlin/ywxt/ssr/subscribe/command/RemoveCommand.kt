package ywxt.ssr.subscribe.command

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.util.config.handleLoadJsonConfigException
import ywxt.ssr.subscribe.util.config.loadConfigFile
import ywxt.ssr.subscribe.util.console.confirm
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.console.sprintln
import ywxt.ssr.subscribe.util.console.wprintln
import java.io.IOException
import java.lang.Exception


class RemoveCommand : CliktCommand(name = "remove", help = "移除订阅源") {

    val sourceIndex: Int by argument(help = "订阅源序号").int()
    val file: String? by option("--file", "-f",help = "配置文件位置")
    override fun run() = runBlocking {
        val config = try {
            loadConfigFile(file)
        }catch (e:Exception){
            handleLoadJsonConfigException(e)
            return@runBlocking
        }
        try {
            if (sourceIndex !in config.sources.indices) {
                eprintln("输入的序号不正确:$sourceIndex")
                return@runBlocking
            }
            val source = config.sources.elementAt(sourceIndex)
            val confirmed = confirm("是否删除订阅源:$source")
            if (confirmed) {
                config.sources.remove(source)
                config.servers.removeAll { it.source == source }
                config.save()
                sprintln("已删除订阅源:$source")
            } else {
                wprintln("删除已取消")
            }
        } catch (e: IOException) {
            eprintln("IO异常:${e.localizedMessage}")
        }

    }


}