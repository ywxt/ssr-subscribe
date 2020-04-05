@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.config

import ywxt.ssr.subscribe.config.ServerConfig


fun Iterable<ServerConfig>.groups(): Map<String, Iterable<ServerConfig>> = this.groupBy { it.group }
    .asSequence()
    .map { group ->
        val key = if (group.key.isBlank()) "未命名" else group.key
        val value = group.value
        Pair(key, value)
    }.associateBy({ it.first }, { it.second })

val ServerConfig.prettyName: String
    get() =
        if (this.remarks.isBlank()) "${this.server}:${this.port}"
        else "${this.remarks} (${this.server}:${this.port})"

