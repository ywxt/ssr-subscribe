import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ywxt.ssr.subscribe.config.SsrServerConfig

class SsrServerConfigTest {
    @Test
    fun load() = runBlocking {
        val config = SsrServerConfig.load("src/test/kotlin/config.json")
        Assert.assertEquals(config.group, "test")
        Assert.assertEquals(config.server, "127.0.0.1")
    }
    @Test
    fun saveTest() = runBlocking {
        val config = SsrServerConfig(
            server = "127.0.0.1",
            port = 8080,
            obfs = "test",
            obfsParam = "test",
            protocol = "test",
            protoParam = "test",
            remarks = "test",
            group = "test",
            password = "test",
            method = "test"
        )
        config.save("src/test/kotlin/config.json")

    }
}