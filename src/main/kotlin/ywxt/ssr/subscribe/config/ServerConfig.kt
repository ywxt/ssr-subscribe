package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import ywxt.ssr.subscribe.ssr.SsrUrl

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
    var obfsParam: String,
    @JsonProperty("proto_param")
    var protoParam: String,
    @JsonProperty("remarks")
    var remarks: String,
    @JsonProperty("group")
    var group: String,
    //local config
    @JsonUnwrapped
    var localConfig: LocalConfig
) {
    companion object {
        fun from(ssrUrl: SsrUrl, localConfig: LocalConfig): ServerConfig = ServerConfig(
            server = ssrUrl.urlBase.server,
            port = ssrUrl.urlBase.port,
            protocol = ssrUrl.urlBase.protocol,
            method = ssrUrl.urlBase.method,
            obfs = ssrUrl.urlBase.obfs,
            password = ssrUrl.urlBase.obfs,
            protoParam = ssrUrl.urlParams.protoParam,
            obfsParam = ssrUrl.urlParams.obfsParam,
            remarks = ssrUrl.urlParams.remarks,
            group = ssrUrl.urlParams.group,
            localConfig = localConfig
        )
    }
}
