import kotlinx.coroutines.runBlocking
import org.junit.Test
import ywxt.ssr.subscribe.async.http.AsyncClient
import java.net.URL

class AsyncClientTest {
    @Test
    fun requestTextTest()= runBlocking{
        val text = AsyncClient().requestPlainText(URL("https://www.baidu.com"))
        assert(text.isNotBlank())
    }

    @Test
    fun requestSsrUrlsTest()= runBlocking{
        val urls = AsyncClient()
            .requestSsrUrls(URL("https://raw.githubusercontent.com/ssrsub/ssr/master/ssrsub"))
        assert(urls.isNotEmpty())
    }
}