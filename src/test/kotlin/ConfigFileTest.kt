import kotlinx.coroutines.runBlocking
import org.junit.Test
import ywxt.ssr.subscribe.async.http.AsyncClient
import ywxt.ssr.subscribe.config.ConfigFile
import ywxt.ssr.subscribe.config.LocalConfig
import ywxt.ssr.subscribe.config.ServerConfig
import java.net.URL

class ConfigFileTest {
    @Test
    fun loadTest() = runBlocking {
        val config = ConfigFile.load("src/test/kotlin/setting.json")
        assert(config.sources.size == 1){"source.size!=1"}
        assert(config.servers.isNotEmpty()){"source.servers is empty"}
        assert(config.servers[0].localConfig.timeout == 3000){"timeout != 3000"}
    }

    @Test
    fun saveTest() = runBlocking {
        val config = ConfigFile.DEFAULT_CONFIG.copy()
        val source = "https://raw.githubusercontent.com/ssrsub/ssr/master/ssrsub"
        config.sources.add(source)
        val urls = AsyncClient()
            .requestSsrUrls(URL("https://raw.githubusercontent.com/ssrsub/ssr/master/ssrsub"))
        config.servers.addAll(
            urls.asSequence()
                .map { ServerConfig.from(it, source, LocalConfig.DEFAULT_LOCAL_CONFIG.copy(timeout = 3000)) })
        config.save("src/test/kotlin/setting.json")
    }

}