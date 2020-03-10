package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

data class ServerConfig(
    @JsonProperty("server")
    var server: String,
    @JsonProperty("server_port")
    var port: Int,
    @JsonProperty("protocol")
    var protocol: String,
    @JsonProperty("method")
    var method: String,
    @JsonProperty("obfs")
    var obfs: String,
    @JsonProperty("password")
    var password: String,
    @JsonProperty("obfs_param")
    var obfsParam:String,
    @JsonProperty("proto_param")
    var protoParam:String,
    @JsonProperty("remarks")
    var remarks:String,
    @JsonProperty("group")
    var group:String,
    //local config
    @JsonUnwrapped
    var localConfig: LocalConfig
)
