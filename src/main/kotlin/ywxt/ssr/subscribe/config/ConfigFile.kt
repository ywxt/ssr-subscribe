package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import ywxt.ssr.subscribe.async.file.AsyncFile
import ywxt.ssr.subscribe.json.JSON_MAPPER
import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

data class ConfigFile(
    @JsonProperty("defaultLocal")
    val defaultLocalConfig: LocalConfig,
    @JsonProperty("servers")
    val servers: MutableSet<ServerConfig>,
    @JsonProperty("sources")
    val sources: MutableSet<String>
) {
    companion object {
        const val FILE_NAME = "setting.json"
        val PATH = Paths.get(System.getProperty("user.home"), ".ssr-sub", FILE_NAME).toString()
        val DEFAULT_CONFIG = ConfigFile(
            defaultLocalConfig = LocalConfig.DEFAULT_LOCAL_CONFIG,
            servers = linkedSetOf(),
            sources = linkedSetOf()
        )

        suspend fun load(path: String = PATH): ConfigFile =
            if (File(path).exists()) {
                AsyncFile(path, StandardOpenOption.READ).use {
                    JSON_MAPPER.readValue<ConfigFile>(it.read())
                }
            } else {
                DEFAULT_CONFIG
            }

    }

    suspend fun save(path: String = PATH) {
        val parent = File(path).parentFile ?: throw IllegalArgumentException("file 必须为有效的文件路径")
        if (!parent.exists()) {
            parent.mkdirs()
        }
        AsyncFile(
            path,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.WRITE)
            .use {
            it.write(JSON_MAPPER.writeValueAsBytes(this))
        }
    }
}