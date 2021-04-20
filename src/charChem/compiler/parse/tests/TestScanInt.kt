package charChem.compiler.parse.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.parse.scanInt
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TestScanInt {
    @Test
    fun testNotNumber() {
        val compiler = createTestCompiler("H2O")
        assertNull(scanInt(compiler))
        assertEquals(compiler.pos, 0)
    }

    @Test
    fun testUnsignedShortInt() {
        val compiler = createTestCompiler("2H2O")
        val k = scanInt(compiler)
        assertNotNull(k)
        assertEquals(k, 2)
        assertEquals(compiler.pos, 1)
    }

    @Test
    fun testUnsignedLongInt() {
        val compiler = createTestCompiler("12345")
        val k = scanInt(compiler)
        assertNotNull(k)
        assertEquals(k, 12345)
        assertEquals(compiler.pos, 5)
    }

    @Test
    fun testSignedShort() {
        val compiler = createTestCompiler("-1")
        val k = scanInt(compiler)
        assertNotNull(k)
        assertEquals(k, -1)
        assertEquals(compiler.pos, 2)
    }

    @Test
    fun testSignedLong() {
        val compiler = createTestCompiler("-123-")
        val k = scanInt(compiler)
        assertNotNull(k)
        assertEquals(k, -123)
        assertEquals(compiler.pos, 4)
    }

    @Test
    fun testMinus() {
        val compiler = createTestCompiler("-")
        val k = scanInt(compiler)
        assertNull(k)
        assertEquals(compiler.pos, 0)
    }
}