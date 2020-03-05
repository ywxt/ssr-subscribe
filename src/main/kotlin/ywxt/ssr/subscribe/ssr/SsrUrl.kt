package ywxt.ssr.subscribe.ssr

class SsrUrl(
    val urlBase: SsrUrlBase,
    val urlParams: SsrUrlParams
)

data class SsrUrlBase(
    val server: String,
    val port: Int,
    val protocol: String,
    val method: String,
    val obfs: String,
    val password: String
)
data class SsrUrlParams(
    val obfsParam:String,
    val protoParam:String,
    val remarks:String,
    val group:String
)