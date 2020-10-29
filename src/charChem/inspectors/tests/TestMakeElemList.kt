package charChem.inspectors.tests

import charChem.compiler.ChemCompiler
import charChem.inspectors.makeElemList
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestMakeElemList() {
    @Test
    fun testSimple() {
        val expr = ChemCompiler("H2O").exec()
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "H2O")
    }
    @Test
    fun testSimpleCharge() {
        val expr = ChemCompiler("SO4^2-").exec()
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "SO4^2-")
    }
    @Test
    fun testNode() {
        val expr = ChemCompiler("CH3CH2COOH").exec()
        assertEquals(expr.getMessage(), "")
        val elList = makeElemList(expr)
        assertEquals(elList.toString(), "C3H6O2")
    }
}