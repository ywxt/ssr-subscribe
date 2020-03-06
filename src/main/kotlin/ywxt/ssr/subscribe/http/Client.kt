package ywxt.ssr.subscribe.http

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import ywxt.ssr.subscribe.exception.HttpException
import ywxt.ssr.subscribe.ssr.SsrUrl
import ywxt.ssr.subscribe.ssr.SsrUrlConvert
import ywxt.ssr.subscribe.util.decodeBase64
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Client {
    companion object {

        const val TIMEOUT = 3000L

        val httpClient = OkHttpClient.Builder()
            .callTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
    }

    suspend fun requestPlainText(url: URL): String {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val body = httpClient.callAsync(request).body
        return requireNotNull(body.use { it?.string() }) { throw HttpException("目标页面无法转换为文本") }
    }

    suspend fun requestSsrUrls(url: URL): List<SsrUrl> {
        val bodyText = requestPlainText(url)
        val ssrUrlsText = bodyText.decodeBase64(false).lineSequence()
        return ssrUrlsText.map { SsrUrlConvert.from(it) }.toList()
    }

}


private suspend fun OkHttpClient.callAsync(request: Request): Response =
    suspendCancellableCoroutine { cont ->
        val call = newCall(request)
        cont.invokeOnCancellation { if (!call.isCanceled()) call.cancel() }
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                cont.resume(response)
            }
        })
    }

