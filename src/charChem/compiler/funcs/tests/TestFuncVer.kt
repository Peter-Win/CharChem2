package charChem.compiler.funcs.tests

import charChem.compiler.funcs.parseVerParameter
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestFuncVer {
    @Test
    fun testParseVerParameter() {
        assertEquals(parseVerParameter(listOf("")), listOf(0, 0))
        assertEquals(parseVerParameter(listOf("1")), listOf(1, 0))
        assertEquals(parseVerParameter(listOf("2.3")), listOf(2, 3))
        assertEquals(parseVerParameter(listOf("1.2.3")), listOf(1, 2))
        assertEquals(parseVerParameter(listOf(" ")), listOf(0, 0))
        assertEquals(parseVerParameter(listOf("a")), listOf(0, 0))
        assertEquals(parseVerParameter(listOf("a.b")), listOf(0, 0))
        assertEquals(parseVerParameter(listOf("4.a")), listOf(4, 0))
        assertEquals(parseVerParameter(listOf("a.5")), listOf(0, 5))
        assertEquals(parseVerParameter(listOf("1","2")), listOf(1, 2))
    }
}