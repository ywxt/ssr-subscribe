import org.junit.Test
import ywxt.ssr.subscribe.ssr.SsrUrlBaseConvert
import ywxt.ssr.subscribe.ssr.SsrUrlConvert
import ywxt.ssr.subscribe.ssr.SsrUrlParamsConvert

class SsrConvertTest {

    @Test
    fun stringToSsrUrlTest() {
        val ssrUrlString = "ssr://172.104.98.12:8097:origin:aes-256-cfb:plain:ZUlXMERuazY5NDU0ZTZuU3d1c3B2OURtUzIwMXRRMEQ/?obfsparam=5LuY6LS5U1NS5rOo5YaMOmh0dHA6Ly90LmNuL0VHSkl5cmw&protoparam=dC5tZS9TU1JTVUI&remarks=QFNTUlNVQi3ml6XmnKwyNy3ku5jotLlTU1LmjqjojZA6dC5jbi9FR0pJeXJs&group=dC5tZS9TU1JTVUI"
        val ssrUrl = SsrUrlConvert.from(ssrUrlString)
        assert(ssrUrl.urlBase.server=="172.104.98.12")
    }
    @Test
    fun stringToSsrUrlBaseTest(){
        val ssrBaseString =  "172.104.98.12:8097:origin:aes-256-cfb:plain:ZUlXMERuazY5NDU0ZTZuU3d1c3B2OURtUzIwMXRRMEQ"
        val ssrBase = SsrUrlBaseConvert.from(ssrBaseString)
        assert(ssrBase.server=="172.104.98.12")
    }
    @Test
    fun stringToSsrUrlParamsTest(){
        val ssrParamsString = "obfsparam=5LuY6LS5U1NS5rOo5YaMOmh0dHA6Ly90LmNuL0VHSkl5cmw&protoparam=dC5tZS9TU1JTVUI&remarks=QFNTUlNVQi3ml6XmnKwyNy3ku5jotLlTU1LmjqjojZA6dC5jbi9FR0pJeXJs&group=dC5tZS9TU1JTVUI"
        val ssrParam = SsrUrlParamsConvert.from(ssrParamsString)
        assert(ssrParam.group.isNotBlank())
    }

}