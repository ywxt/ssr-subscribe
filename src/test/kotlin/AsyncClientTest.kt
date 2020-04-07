import org.junit.Test
import ywxt.ssr.subscribe.async.http.AsyncClient
import java.net.URL

class AsyncClientTest {
    @Test
    suspend fun requestTextTest(){
        val text = AsyncClient().requestPlainText(URL("Https://www.baidu.com"))
        assert(text.isBlank())
    }

    @Test
    suspend fun requestSsrUrlsTest(){
        val urls = AsyncClient()
            .requestSsrUrls(URL("https://raw.githubusercontent.com/ssrsub/ssr/master/ssrsub"))
        assert(urls.isNotEmpty())
    }
}