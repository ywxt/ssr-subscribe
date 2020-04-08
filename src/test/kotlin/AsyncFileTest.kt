import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ywxt.ssr.subscribe.async.file.AsyncFile

class AsyncFileTest{
    @Test
    fun readBytesTest() = runBlocking{
        writeBytesTest()
        val bytes = AsyncFile("src/test/kotlin/test.txt").use {
            it.read()
        }
        Assert.assertNotNull(bytes)
        assert(bytes.isNotEmpty())
        assert(String(bytes)=="Hello world")
    }

    @Test
    fun writeBytesTest() = runBlocking {
        val bytes = "Hello world".toByteArray()
        AsyncFile("src/test/kotlin/test.txt").use {
            it.write(bytes)
        }
    }
    @Test
    fun writeStringTest() = runBlocking {
        val string = """Hello worldHello worldHello worldHello worldHello worldHello worldHello
| worldHello worldHello worldHello worldHello worldHello world""".trimMargin()
        AsyncFile("src/test/kotlin/test.txt").use {
            it.writeString(string)
        }
    }
    @Test
    fun readStringTest()= runBlocking {
        writeStringTest()
        val string = AsyncFile("src/test/kotlin/test.txt").use {
            it.readString()
        }
        assert(string=="""Hello worldHello worldHello worldHello worldHello worldHello worldHello
| worldHello worldHello worldHello worldHello worldHello world""".trimMargin())
    }
}