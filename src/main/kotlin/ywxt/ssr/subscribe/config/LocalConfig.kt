package ywxt.ssr.subscribe.config

import com.fasterxml.jackson.annotation.JsonProperty

data class LocalConfig(
    @JsonProperty("local_address")
    val localAddress: String = "127.0.0.1",
    @JsonProperty("local_port")
    val localPort: Int = 1080,
    @JsonProperty("timeout")
    val timeout: Int? = null,
    @JsonProperty("udp_timeout")
    val udpTimeout: Int? = null,
    @JsonProperty("udp_cache")
    val udpCache: Int? = null,
    @JsonProperty("fast_open")
    val fastOpen: Boolean? = null,
    @JsonProperty("redirect")
    val redirect: String? = null
)
val DEFAULT_LOCAL_CONFIG = LocalConfig()
