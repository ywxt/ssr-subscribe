@file:Suppress("PackageDirectoryMismatch")
package ywxt.ssr.subscribe.util.ssrurl


import ywxt.ssr.subscribe.ssr.SsrUrl

inline fun Iterable<SsrUrl>.groups(): Map<String, Iterable<SsrUrl>> = this.groupBy { it.urlParams.group }
    .asSequence()
    .map { group ->
        val key = if (group.key.isBlank()) "未命名" else group.key
        val value = group.value
        Pair(key, value)
    }.associateBy({ it.first }, { it.second })


val SsrUrl.prettyName: String
    get() =
        if (this.urlParams.remarks.isBlank()) "${this.urlBase.server}:${this.urlBase.port}"
        else "${this.urlParams.remarks} (${this.urlBase.server}:${this.urlBase.port})"



