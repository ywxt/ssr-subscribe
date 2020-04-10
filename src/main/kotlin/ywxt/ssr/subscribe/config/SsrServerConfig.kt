package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

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
){
    //local config
    @JsonUnwrapped
    var localConfig: LocalConfig = LocalConfig.DEFAULT_LOCAL_CONFIG.copy()
}