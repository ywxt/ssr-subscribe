package ywxt.ssr.subscribe.command

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.util.console.confirm
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.console.sprintln
import ywxt.ssr.subscribe.util.console.wprintln
import java.io.IOException


class RemoveCommand : CliktCommand(name = "remove",help = "移除订阅源") {

    val sourceIndex: Int by argument(help = "订阅源序号").int()

    override fun run() = runBlocking {
        try {
            val config = ConfigFile.load();
            if (sourceIndex !in config.sources.indices) {
                eprintln("输入的序号不正确:$sourceIndex")
                return@runBlocking
            }
            val source =  config.sources.elementAt(sourceIndex)
            val confirmed = confirm("是否删除订阅源:$source")
            if (confirmed){
                config.sources.remove(source)
                config.servers.removeAll { it.source==source }
                config.save()
                sprintln("已删除订阅源:$source")
            }else{
                wprintln("删除已取消")
            }
        }catch (_: JsonProcessingException){
            eprintln("文件格式不正确，请检查文件${ConfigFile.PATH}")
        }catch (e: IOException){
            eprintln("IO异常:${e.localizedMessage}")
        }

    }


}