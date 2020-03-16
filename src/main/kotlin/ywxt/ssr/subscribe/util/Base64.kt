@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.base64

import java.util.*

fun String.decodeBase64(webSafe: Boolean = true): String {
    if (this.isBlank()) return ""
    val base64String = if (webSafe) {
        val tmp = this.replace('-', '+')
            .replace('_', '/')
        val padding = tmp.length * 3 % 4
        if (padding != 0) {
            tmp.padEnd(tmp.length + padding, '=')
        } else {
            tmp
        }
    } else {
        this
    }
    return String(Base64.getDecoder().decode(base64String))
}

fun String.encodeBase64(webSafe: Boolean = true): String {
    if (this.isEmpty()) return ""
    val base64String = Base64.getEncoder().encodeToString(this.toByteArray())
    return if (webSafe) {
        base64String.replace('+', '-')
            .replace('/', '_')
            .trimEnd('=')
    } else {
        base64String
    }
}