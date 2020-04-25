package ywxt.ssr.subscribe

import com.github.ajalt.clikt.core.subcommands
import ywxt.ssr.subscribe.command.*

fun main(args: Array<String>) =
    MainCommand().subcommands(
        AddSourceCommand(),
        SourceCommand(),
        RemoveCommand(),
        UpdateCommand(),
        ServerCommand(),
        SwitchServerCommand(),
        ShowCommand()
    ).main(args)
