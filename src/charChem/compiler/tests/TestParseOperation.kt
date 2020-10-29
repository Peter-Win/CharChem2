package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.parseOperation
import charChem.core.ChemOp
import org.testng.annotations.Test
import kotlin.test.*

class TestParseOperation {
    @Test
    fun testMiss() {
        val c = ChemCompiler("O2 + H2")
        c.prepareText()
        val res = parseOperation(c)
        assertEquals(res, -1)
        assertEquals(c.curChar(), 'O')
    }
    @Test
    fun testPlus() {
        val c = ChemCompiler("O2 + H2")
        c.prepareText()
        c.pos += 3
        assertEquals(c.curChar(), '+')
        val res = parseOperation(c)
        assertEquals(res, 0)
        assertNotNull(c.curEntity)
        val op = c.curEntity
        assertTrue(op is ChemOp)
        assertEquals(op.srcText, "+")
        assertEquals(op.dstText, "+")
        assertFalse(op.eq)
        assertEquals(c.curChar(), ' ')
        assertEquals(c.skipSpace(), 'H')
    }
    @Test
    fun testShortArrow() {
        val c = ChemCompiler("CuO -> Cu2O")
        c.prepareText()
        c.pos += 4
        assertEquals(c.curChar(), '-')
        val res = parseOperation(c)
        assertEquals(res, 0)
        val op = c.curEntity
        assertTrue(op is ChemOp)
        assertEquals(op.srcText, "->")
        assertEquals(op.dstText, "→")
        assertTrue(op.eq)
        assertEquals(c.curChar(), ' ')
        assertEquals(c.skipSpace(), 'C')
    }
    @Test
    fun testLongArrow() {
        val c = ChemCompiler("CuO --> Cu2O")
        c.prepareText()
        c.pos += 4
        assertEquals(c.curChar(), '-')
        val res = parseOperation(c)
        assertEquals(res, 0)
        val op = c.curEntity
        assertTrue(op is ChemOp)
        assertEquals(op.srcText, "-->")
        assertEquals(op.dstText, "—→")
        assertTrue(op.eq)
        assertEquals(c.curChar(), ' ')
        assertEquals(c.skipSpace(), 'C')
    }
    @Test
    fun testLastOp() {
        val c = ChemCompiler("CuO -->")
        c.prepareText()
        c.pos += 4
        val res = parseOperation(c)
        assertEquals(res, 0)
        val op = c.curEntity
        assertTrue(op is ChemOp)
        assertEquals(op.srcText, "-->")
    }
}