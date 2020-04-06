package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import ywxt.ssr.subscribe.ssr.ServerConfigConvert
import ywxt.ssr.subscribe.ssr.SsrUrl
import ywxt.ssr.subscribe.ssr.SsrUrlConvert

data class ServerConfig(
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
    val group: String,
    @JsonProperty("source")
    val source:String,
    //local config
    @JsonUnwrapped
    var localConfig: LocalConfig
) {
    companion object {
        fun from(ssrUrl: SsrUrl, source: String ,localConfig: LocalConfig): ServerConfig = ServerConfig(
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
            source = source,
            localConfig = localConfig
        )
    }

    override fun toString(): String = """
        SSR链接:${ServerConfigConvert.to(this)}
        服务器IP:${server}
        端口号:${port}
        密码:${password}
        协议:${protocol}
        方式:${method}
        obfs:${obfs}
        协议参数:${obfsParam},
        方法参数:${method},
        remark:${remarks},
        分组:${group}
    """.trimIndent()
}
