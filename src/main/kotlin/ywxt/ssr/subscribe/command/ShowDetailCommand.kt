package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option


class ShowDetailCommand : CliktCommand(name = "show") {

    val name:String by argument()

    val server: Boolean by option("--serve").flag(default = false)
    val source:Boolean by option("--source","-s").flag(default = true)
    val group:Boolean by option("--group","-g").flag(default = false)
    override fun run() {
        TODO("Not yet implemented")
    }


}