package ywxt.ssr.subscribe.ssr

import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.config.SsrServerConfig
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
                obfsParam = kvs.getValue("obfsparam"),
                protoParam = kvs.getValue("protoparam"),
                remarks = kvs.getValue("remarks"),
                group = kvs.getValue("group")

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
        val ssrUrl = try {
            if (value.contains("/?")) value.substring(6) else value.substring(6).decodeBase64()
        } catch (_: Exception) {
            throw ParseException("SSR地址有误:${value}")
        }
        val couple = ssrUrl.split("/?", limit = 2)
        if (couple.size != 2) {
            throw ParseException("SSR地址有误:${value}")
        }
        try {
            return SsrUrl(
                urlBase = SsrUrlBaseConvert.from(couple[0]),
                urlParams = SsrUrlParamsConvert.from(couple[1])
            )
        } catch (e: ParseException) {
            throw ParseException("SSR地址有误:${value}")
        }
    }

    override fun to(type: SsrUrl): String = type.run {
        "ssr://${SsrUrlBaseConvert.to(urlBase)}/?${SsrUrlParamsConvert.to(urlParams)}"
    }

}

object ServerConfigConvert : Convert<ServerConfig, String> {
    override fun from(value: String): ServerConfig {
        TODO("Not implemented")
    }

    override fun to(type: ServerConfig): String = SsrUrlConvert.to(
        SsrUrl(
            SsrUrlBase(
                server = type.server,
                port = type.port,
                protocol = type.protocol,
                method = type.method,
                obfs = type.obfs,
                password = type.password
            ),
            SsrUrlParams(
                obfsParam = type.obfsParam,
                protoParam = type.protoParam,
                remarks = type.remarks,
                group = type.group
            )
        )
    )

}

object SsrServerConfigConvert : Convert<SsrServerConfig, ServerConfig> {
    override fun from(value: ServerConfig): SsrServerConfig = SsrServerConfig(
        server = value.server,
        port = value.port,
        method = value.method,
        protocol = value.protocol,
        obfs = value.obfs,
        password = value.password,
        obfsParam = value.obfsParam,
        protoParam = value.protoParam,
        remarks = value.remarks,
        group = value.group,
        localConfig = value.localConfig
    )

    override fun to(type: SsrServerConfig): ServerConfig {
        TODO("Not yet implemented")
    }

}
