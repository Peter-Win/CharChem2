package charChem.compiler2.parse.tests

import charChem.compiler2.createTestCompiler
import charChem.compiler2.parse.scanOp
import org.testng.annotations.Test
import kotlin.test.*

class TestScanOp {
    @Test
    fun testLong() {
        val compiler = createTestCompiler("-->")
        val res = scanOp(compiler)
        assertNotNull(res)
        assertEquals(res.src, "-->")
        assertEquals(compiler.pos, 3)
    }
    @Test
    fun testShort() {
        val compiler = createTestCompiler("->")
        val res = scanOp(compiler)
        assertNotNull(res)
        assertEquals(res.src, "->")
        assertEquals(compiler.pos, 2)
        assertTrue(res.div)
    }
    @Test
    fun testPlus() {
        val compiler = createTestCompiler("O2 + H2")
        val p1 = scanOp(compiler)
        assertNull(p1)
        assertEquals(compiler.pos, 0)
        compiler.pos = 3
        val p2 = scanOp(compiler)
        assertNotNull(p2)
        assertEquals(p2.src, "+")
        assertFalse(p2.div)
        assertEquals(compiler.pos, 4)
    }
    @Test
    fun testNE() {
        val compiler = createTestCompiler("!=")
        val res = scanOp(compiler)
        assertNotNull(res)
        assertEquals(res.src, "!=")
        assertEquals(compiler.pos, 2)
        assertTrue(res.div)
    }
}