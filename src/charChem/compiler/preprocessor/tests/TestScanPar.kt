package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.scanPar
import org.testng.annotations.Test
import kotlin.test.assertEquals

private fun getPar(src: String, pos: Int): String {
    val end = scanPar(src, pos)
    return src.substring(pos, end)
}

class TestScanPar {
    @Test
    fun testComma() {
        assertEquals(getPar("hello,world", 0), "hello")
        assertEquals(getPar("hello,world", 6), "world")
    }
    @Test
    fun testBracket() {
        assertEquals(getPar("(abc,d)", 1), "abc")
        assertEquals(getPar("(abc,d)", 5), "d")
    }
    @Test
    fun testNestedBracket() {
        assertEquals(getPar("a,rgb(255,0,0),z", 2), "rgb(255,0,0)")
        assertEquals(getPar("A(B(x,y), C(1,2)), Z()", 0), "A(B(x,y), C(1,2))")
        assertEquals(getPar("(first(second(a,b)))", 1), "first(second(a,b))")
    }
    @Test
    fun testComment() {
        assertEquals(getPar("\"hello,world\"", 0), "\"hello,world\"")
        assertEquals(getPar("\"hello,world\",second", 0), "\"hello,world\"")
        assertEquals(getPar("""print("a,b,c",2)""", 0), """print("a,b,c",2)""")
        assertEquals(getPar("""print(")")""", 0), """print(")")""")
    }
}