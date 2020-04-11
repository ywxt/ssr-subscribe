package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.module.kotlin.readValue
import ywxt.ssr.subscribe.async.file.AsyncFile
import ywxt.ssr.subscribe.json.JSON_MAPPER
import java.nio.file.StandardOpenOption

data class SsrServerConfig(
    @JsonProperty("server")
    val server: String,
    @JsonProperty("server_port")
    val port: Int,
    @JsonProperty("protocol")
    val protocol: String,
    @JsonProperty("method")
    val method: String,
    @JsonProperty("obfs")
    val obfs: String,
    @JsonProperty("password")
    val password: String,
    @JsonProperty("obfs_param")
    val obfsParam: String,
    @JsonProperty("proto_param")
    val protoParam: String,
    @JsonProperty("remarks")
    val remarks: String,
    @JsonProperty("group")
    val group: String
) {
    //local config
    @JsonUnwrapped
    var localConfig: LocalConfig = LocalConfig.DEFAULT_LOCAL_CONFIG.copy()

    companion object {
        const val DEFAULT_PATH = "config.json"
        suspend fun load(path: String = DEFAULT_PATH): SsrServerConfig =
            AsyncFile(path, StandardOpenOption.READ).use {
                val config = it.read()
                JSON_MAPPER.readValue<SsrServerConfig>(config)
            }
    }

    suspend fun save(path: String = DEFAULT_PATH) {
        AsyncFile(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)
            .use {
                val config = JSON_MAPPER.writeValueAsBytes(this)
                it.write(config)
            }
    }
}