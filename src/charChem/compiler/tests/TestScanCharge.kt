package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.scanCharge
import org.testng.annotations.Test
import kotlin.test.*

class TestScanCharge {
    @Test
    fun testSuccessNum() {
        val c = ChemCompiler("O^2--")
        c.prepareText()
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
        val c = ChemCompiler("O^+-")
        c.prepareText()
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
        val c = ChemCompiler("O^-+")
        c.prepareText()
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
        val c = ChemCompiler("O^----")
        c.prepareText()
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
        val c = ChemCompiler("O^--")
        c.prepareText()
        assertEquals(c.curChar(), 'O')
        val charge = scanCharge(c, false)
        assertNull(charge)
    }
    @Test
    fun testNoChargeLast() {
        val c = ChemCompiler("O^--")
        c.prepareText()
        c.pos = 5
        assertTrue(c.isFinish())
        val charge = scanCharge(c, false)
        assertNull(charge)
    }
}