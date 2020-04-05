package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.runBlocking
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.util.console.confirm
import ywxt.ssr.subscribe.util.console.eprintln
import ywxt.ssr.subscribe.util.console.sprintln
import ywxt.ssr.subscribe.util.console.wprintln


class RemoveCommand : CliktCommand(name = "remove") {

    val sourceIndex: Int by argument().int()

    override fun run() = runBlocking {
        val config = ConfigFile.load();
        if (sourceIndex !in config.sources.indices) {
            eprintln("输入的序号不正确:$sourceIndex")
            return@runBlocking
        }
        val source =  config.sources[sourceIndex]
        val confirmed = confirm("是否删除订阅源:$source")
        if (confirmed){
            config.sources.removeAt(sourceIndex)
            config.servers.removeAll { it.source==source }
            sprintln("已删除订阅源:$source")
        }else{
            wprintln("删除已取消")
        }

    }


}