package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty

data class LocalConfig(
    @JsonProperty("local_address")
    var localAddress: String = "127.0.0.1",
    @JsonProperty("local_port")
    var localPort: Int = 1080,
    @JsonProperty("timeout")
    var timeout: Int?,
    @JsonProperty("udp_timeout")
    var udpTimeout: Int?,
    @JsonProperty("udp_cache")
    var udpCache: Int?,
    @JsonProperty("fast_open")
    var fastOpen: Boolean?,
    @JsonProperty("redirect")
    var redirect: String?
)
