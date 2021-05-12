package charChem.compiler.main.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.main.UniBondParam
import charChem.compiler.main.calcBondDirection
import charChem.compiler.main.makeParamsDict
import charChem.compiler.main.parseBondMultiplicity
import charChem.core.ChemBond
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestUniversalBond {
    @Test
    fun testMakeParamsDict() {
        assertEquals(makeParamsDict(listOf(), listOf()), mapOf())
        assertEquals(makeParamsDict(listOf("x1", "y-1"), listOf(0, 9)), mapOf(
                'x' to UniBondParam('x', "1", 1), // value position = arg position + 1
                'y' to UniBondParam('y', "-1", 10),
        ))
        assertEquals(makeParamsDict(listOf("", "a0"), listOf(2, 4)),
                mapOf('a' to UniBondParam('a', "0", 5)))
        assertEquals(makeParamsDict(listOf("Crgb(255,0,0)"), listOf(11)),
                mapOf('C' to UniBondParam('C', "rgb(255,0,0)", 12)))
    }

    @Test
    fun testCalcBondDirection() {
        val compiler = createTestCompiler("")
        // _(A90)
        assertEquals(calcBondDirection(compiler, mapOf(
                'A' to UniBondParam('A', "90", 22)
        )), Point(0.0, 1.0))
        // _(A180,L1.5)
        assertEquals(calcBondDirection(compiler, mapOf(
                'A' to UniBondParam('A', "180", 33),
                'L' to UniBondParam('L', "1.5", 44),
        )), Point(-1.5, 0.0))
        // _(x.7,y-.9)
        assertEquals(calcBondDirection(compiler, mapOf(
                'x' to UniBondParam('x', ".7", 2),
                'y' to UniBondParam('y', "-.9", 3),
        )), Point(0.7, -0.9))
    }

    @Test
    fun testParseBondMultiplicity2() {
        val compiler = createTestCompiler("")
        val bond1 = ChemBond()
        parseBondMultiplicity(compiler, bond1, UniBondParam('N', "2", 1))
        assertEquals(bond1.n, 2.0)
        assertNull(bond1.align)
    }
    @Test
    fun testParseBondMultipicity15() {
        val compiler = createTestCompiler("")
        val bond15 = ChemBond()
        parseBondMultiplicity(compiler, bond15, UniBondParam('N', "1.5", 1))
        assertEquals(bond15.n, 1.5)
        assertNull(bond15.align)
    }
    @Test
    fun testParseBondMulX() {
        val compiler = createTestCompiler("")
        val bondX = ChemBond()
        parseBondMultiplicity(compiler, bondX, UniBondParam('N', "2x", 2))
        assertEquals(bondX.n, 2.0)
        assertEquals(bondX.style, "x")
        assertTrue(bondX.isCross())
    }
    @Test
    fun testParseBondMulL() {
        val compiler = createTestCompiler("")
        val bondL = ChemBond()
        parseBondMultiplicity(compiler, bondL, UniBondParam('N', "2L", 3))
        assertEquals(bondL.n, 2.0)
        assertEquals(bondL.align, 'l')
    }
    @Test
    fun testParseBondMulR() {
        val compiler = createTestCompiler("")
        val bondR = ChemBond()
        parseBondMultiplicity(compiler, bondR, UniBondParam('N', "2r", 4))
        assertEquals(bondR.n, 2.0)
        assertEquals(bondR.align, 'r')
    }
    @Test
    fun testParseBondMulM() {
        val compiler = createTestCompiler("")
        val bondM = ChemBond()
        parseBondMultiplicity(compiler, bondM, UniBondParam('N', "2M", 5))
        assertEquals(bondM.n, 2.0)
        assertEquals(bondM.align, 'm')
    }
}