package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import ywxt.ssr.subscribe.async.file.AsyncFile
import ywxt.ssr.subscribe.json.JSON_MAPPER
import java.io.File

data class ConfigFile(
    @JsonProperty("defaultLocal")
    val defaultLocalConfig: LocalConfig,
    @JsonProperty("servers")
    val servers: MutableList<ServerConfig>,
    @JsonProperty("sources")
    val sources: MutableList<String>
) {
    companion object {
        const val FILE_NAME = "setting.json"
        const val PATH = "~/.ssr-sub/"
        val DEFAULT_CONFIG = ConfigFile(
            defaultLocalConfig = LocalConfig(),
            servers = mutableListOf(),
            sources = mutableListOf()
        )

        suspend fun load(file: String = PATH + FILE_NAME): ConfigFile =
            if (File(file).exists()) {
                AsyncFile(file).use {
                    JSON_MAPPER.readValue<ConfigFile>(it.read())
                }
            } else {
                DEFAULT_CONFIG
            }

    }

    suspend fun save(file: String = PATH + FILE_NAME) = AsyncFile(file).use {
        it.write(JSON_MAPPER.writeValueAsBytes(this))
    }

}