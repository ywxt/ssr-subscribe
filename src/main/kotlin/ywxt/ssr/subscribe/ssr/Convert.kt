package ywxt.ssr.subscribe.ssr

import ywxt.ssr.subscribe.exception.ParseException
import ywxt.ssr.subscribe.util.base64.decodeBase64
import ywxt.ssr.subscribe.util.base64.encodeBase64
import java.lang.Exception

interface Convert<T, V> {
    fun from(value: V): T
    fun to(type: T): V
}

object SsrUrlBaseConvert : Convert<SsrUrlBase, String> {
    override fun from(value: String): SsrUrlBase {
        try {
            val parts = value.split(':', limit = 6)
            return SsrUrlBase(
                server = parts[0],
                port = parts[1].toInt(),
                protocol = parts[2],
                method = parts[3],
                obfs = parts[4],
                password = parts[5].decodeBase64()
            )
        } catch (_: Exception) {
            throw ParseException("SSR地址有误")
        }
    }

    override fun to(type: SsrUrlBase): String = type.run {
        "${server}:${port}:${protocol}:${method}:${obfs}:${password.encodeBase64()}"
    }

}

object SsrUrlParamsConvert : Convert<SsrUrlParams, String> {
    override fun from(value: String): SsrUrlParams {
        try {
            val kvs = value.split('&').asSequence()
                .map { it.split('=') }.associateBy({ it[0] }, { it[1].decodeBase64() })
            return SsrUrlParams(
                obfsParam = kvs.getValue("obfsparam").decodeBase64(),
                protoParam = kvs.getValue("protoparam").decodeBase64(),
                remarks = kvs.getValue("remarks").decodeBase64(),
                group = kvs.getValue("group").decodeBase64()

            )
        } catch (_: Exception) {
            throw ParseException("SSR地址有误")
        }
    }

    override fun to(type: SsrUrlParams): String = type.run {
        "obfsparam=${obfsParam.encodeBase64()}&protoparam=${protoParam.encodeBase64()}&remarks=${remarks.encodeBase64()}&group=${group.encodeBase64()}"
    }

}

object SsrUrlConvert : Convert<SsrUrl, String> {
    override fun from(value: String): SsrUrl {
        if (!value.startsWith("ssr://")) {
            throw ParseException("非SSR地址:${value}")
        }
        val couple = value.substring(6).split("/?", limit = 2)
        if (couple.size != 2) {
            throw ParseException("SSR地址有误:${value}")
        }
        try {
            return SsrUrl(
                urlBase = SsrUrlBaseConvert.from(couple[0]),
                urlParams = SsrUrlParamsConvert.from(couple[1])
            )
        }catch (e:ParseException){
            throw ParseException("SSR地址有误:${value}")
        }
    }

    override fun to(type: SsrUrl): String = type.run {
        "ssr://${SsrUrlBaseConvert.to(urlBase)}/?${SsrUrlParamsConvert.to(urlParams)}"
    }

}