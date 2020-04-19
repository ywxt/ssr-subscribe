@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.config

import com.fasterxml.jackson.core.JsonProcessingException
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.config.SsrServerConfig
import ywxt.ssr.subscribe.util.console.eprintln
import java.io.IOException
import java.lang.Exception


fun Iterable<ServerConfig>.groups(): Map<String, List<ServerConfig>> = this.groupBy { it.group }
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


suspend fun loadConfigFile(file: String? = null): ConfigFile =
    if (file.isNullOrBlank()) {
        ConfigFile.load()
    } else {
        ConfigFile.load(file)
    }

@Suppress("NOTHING_TO_INLINE")
inline fun handleLoadJsonConfigException(e: Exception) {
    when (e) {
        is JsonProcessingException -> eprintln(
            "配置文件无法解析"
        )
        is NoSuchFileException -> eprintln("未找到配置文件")
        is IOException -> eprintln("IO异常:${e.localizedMessage}")
        else -> throw e
    }
}

suspend fun loadSsrServerConfig(file: String? = null): SsrServerConfig =
    if (file.isNullOrBlank()) {
        SsrServerConfig.load()
    } else {
        SsrServerConfig.load(file)
    }