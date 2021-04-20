package charChem.compiler.parse.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.parse.scanArgs
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestScanArgs {
    @Test
    fun testSingle() {
        val c = createTestCompiler("a(B)c")
        c.pos = 2
        assertEquals(c.curChar(), 'B')
        val (args, pos) = scanArgs(c)
        assertEquals(c.curChar(), 'c')
        assertEquals(args, listOf("B"))
        assertEquals(pos, listOf(2))
    }
    @Test
    fun testEmpty() {
        val c = createTestCompiler("a()c")
        c.pos = 2
        assertEquals(c.curChar(), ')')
        val (args, pos) = scanArgs(c)
        assertEquals(c.curChar(), 'c')
        assertEquals(pos, listOf())
        assertEquals(args, listOf())
    }
    @Test
    fun testThree() {
        val c = createTestCompiler("a(one,two,three)e")
        c.pos = 2
        val (args, pos) = scanArgs(c)
        assertEquals(c.curChar(), 'e')
        assertEquals(args, listOf("one", "two", "three"))
        assertEquals(pos, listOf(2,6,10))
    }
    @Test
    fun testNested() {
        val c = createTestCompiler("_(A(),B(x),C(D(y),E(z)))e")
        c.pos = 2
        val (args, pos) = scanArgs(c)
        assertEquals(c.curChar(), 'e')
        assertEquals(args, listOf("A()", "B(x)", "C(D(y),E(z))"))
        assertEquals(pos, listOf(2,6,11))
    }
    @Test
    fun testNotClosed() {
        val c = createTestCompiler("_(hello")
        c.pos = 2
        val err = assertFails { scanArgs(c) }
        Lang.curLang = "ru"
        assertEquals(err.message, "Необходимо закрыть скобку, открытую в позиции 2")
    }
    @Test
    fun testNotClosedNested() {
        val c = createTestCompiler("_(hello()")
        c.pos = 2
        val err = assertFails { scanArgs(c) }
        Lang.curLang = "ru"
        assertEquals(err.message, "Необходимо закрыть скобку, открытую в позиции 2")
    }
}