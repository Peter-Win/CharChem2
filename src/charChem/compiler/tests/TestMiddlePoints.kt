package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemBond
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals

private fun bondNodes(bond: ChemBond) = bond.nodes.map { (it?.index ?: -1) + 1 }

class TestMiddlePoints {
    @Test
    fun testSimple() {
        val expr = compile("H2C`|H2C`|H2C_m(x2)_m(y2)_(x-2)")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H6")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bondNodes(bonds[0]), listOf(1, 2))
        assertEquals(bondNodes(bonds[1]), listOf(2, 3))
        assertEquals(bondNodes(bonds[2]), listOf(3, 1))
    }
    @Test
    fun testInvalidMidpointBeforeNode() {
        val expr = compile("|_m(x1)_m(y-1)C")
        assertEquals(expr.getMessage("en"), "Invalid middle point")
        assertEquals(expr.error!!.params!!.find { it.first == "pos" }!!.second, 2)
    }
    @Test
    fun testInvalidMidpointInTheEndOfAgent() {
        val expr = compile("||_m(x1)_m(y-1)")
        assertEquals(expr.getMessage("en"), "Invalid middle point")
        assertEquals(expr.error!!.params!!.find { it.first == "pos" }!!.second, 3)
    }
    @Test
    fun testNotMergeBondWithMiddlePoints() {
        //   ---1<--
        // 2/   |0   \1
        //  \   v    /
        //   -->2---
        val expr = compile("|_m(x1)_m(y-1)`-_m(x-1)_m(y1)-")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds.size, 3)
        assertEquals(bondNodes(bonds[0]), listOf(1, 2))
        assertEquals(bondNodes(bonds[1]), listOf(2, 1))
        assertEquals(bondNodes(bonds[2]), listOf(1, 2))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C2H2")
    }
}