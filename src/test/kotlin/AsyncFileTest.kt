import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import ywxt.ssr.subscribe.async.file.AsyncFile

class AsyncFileTest{
    @Test
    fun readBytesTest() = runBlocking{
        writeBytesTest()
        val bytes = AsyncFile("test.txt").use {
            it.read()
        }
        Assert.assertNotNull(bytes)
        assert(bytes.isNotEmpty())
        assert(String(bytes)=="Hello world")
    }

    @Test
    fun writeBytesTest() = runBlocking {
        val bytes = "Hello world".toByteArray()
        AsyncFile("test.txt").use {
            it.write(bytes)
        }
    }
    @Test
    fun writeStringTest() = runBlocking {
        val string = "Hello world"
        AsyncFile("test.txt").use {
            it.writeString(string)
        }
    }
    @Test
    fun readStringTest()= runBlocking {
        writeStringTest()
        val string = AsyncFile("text.txt").use {
            it.readString()
        }
        assert(string=="Hello world")
    }
}