package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.makeElemList
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestMakeElemList() {
    @Test
    fun testSimple() {
        val expr = compile("H2O")
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "H2O")
    }
    @Test
    fun testSimpleCharge() {
        val expr = compile("SO4^2-")
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "SO4^2-")
    }
    @Test
    fun testNode() {
        val expr = compile("CH3CH2COOH")
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "C3H6O2")
    }
    @Test
    fun testBracketsWithCharge() {
        val expr = compile("[Fe(CN)6]2^3-")
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "Fe2C12N12^6-")
    }
}