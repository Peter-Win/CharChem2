package charChem.compiler.parse.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.parse.scanCharge
import org.testng.annotations.Test
import kotlin.test.*

class TestScanCharge {
    @Test
    fun testSuccessNum() {
        val c = createTestCompiler("O^2--")
        c.pos += 2
        assertEquals(c.curChar(), '2')
        val charge = scanCharge(c, false)
        assertNotNull(charge)
        assertEquals(charge.value, -2.0)
        assertEquals(charge.text, "2-")
        assertFalse(charge.isLeft)
        assertEquals(c.curChar(), '-')
    }
    @Test
    fun testSuccessPlus1() {
        val c = createTestCompiler("O^+-")
        c.pos += 2
        assertEquals(c.curChar(), '+')
        val charge = scanCharge(c, false)
        assertNotNull(charge)
        assertEquals(charge.value, 1.0)
        assertEquals(charge.text, "+")
        assertEquals(c.curChar(), '-')
    }
    @Test
    fun testSuccessMinus1() {
        val c = createTestCompiler("O^-+")
        c.pos += 2
        assertEquals(c.curChar(), '-')
        val charge = scanCharge(c, false)
        assertNotNull(charge)
        assertEquals(charge.value, -1.0)
        assertEquals(charge.text, "-")
        assertEquals(c.curChar(), '+')
    }
    @Test
    fun testSuccessMinus3() {
        val c = createTestCompiler("O^----")
        c.pos += 2
        assertEquals(c.curChar(), '-')
        val charge = scanCharge(c, false)
        assertNotNull(charge)
        assertEquals(charge.value, -3.0)
        assertEquals(charge.text, "---")
        assertEquals(c.curChar(), '-')
    }
    @Test
    fun testNoCharge() {
        val c = createTestCompiler("O^--")
        assertEquals(c.curChar(), 'O')
        val charge = scanCharge(c, false)
        assertNull(charge)
    }
    @Test
    fun testNoChargeLast() {
        val c = createTestCompiler("O^--")
        c.pos = 5
        assertTrue(c.isFinish())
        val charge = scanCharge(c, false)
        assertNull(charge)
    }
}