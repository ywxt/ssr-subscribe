import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ywxt.ssr.subscribe.async.file.AsyncFile
import java.nio.file.StandardOpenOption

class AsyncFileTest {
    @Test
    fun readBytesTest() = runBlocking {
        writeBytesTest()
        val bytes = AsyncFile("src/test/kotlin/test.txt", StandardOpenOption.READ).use {
            it.read()
        }
        Assert.assertNotNull(bytes)
        assert(bytes.isNotEmpty())
        assert(String(bytes) == "Hello world")
    }

    @Test
    fun writeBytesTest() = runBlocking {
        val bytes = "Hello world".toByteArray()
        AsyncFile(
            "src/test/kotlin/test.txt",
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        ).use {
            it.write(bytes)
        }
    }

    @Test
    fun writeStringTest() = runBlocking {
        val string = """Hello worldHello worldHello worldHello worldHello worldHello worldHello
| worldHello worldHello worldHello worldHello worldHello world""".trimMargin()
        AsyncFile(
            "src/test/kotlin/test.txt",
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        ).use {
            it.writeString(string)
        }
    }

    @Test
    fun readStringTest() = runBlocking {
        writeStringTest()
        val string = AsyncFile("src/test/kotlin/test.txt", StandardOpenOption.READ).use {
            it.readString()
        }
        assert(
            string == """Hello worldHello worldHello worldHello worldHello worldHello worldHello
| worldHello worldHello worldHello worldHello worldHello world""".trimMargin()
        )
    }
}