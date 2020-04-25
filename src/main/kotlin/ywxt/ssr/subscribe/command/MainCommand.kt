package ywxt.ssr.subscribe.command

import com.github.ajalt.clikt.core.CliktCommand

class MainCommand : CliktCommand(name = "ssr-sub", help = "SSR订阅工具", invokeWithoutSubcommand = true) {
    override fun run() = if (currentContext.invokedSubcommand == null) {
        println("实验竞赛月参赛作品")
        println("名称: SSR订阅工具")
        println("github: https://github.com/ywxt/ssr-subscribe")
        println()
        println(getFormattedHelp())
    } else Unit

}