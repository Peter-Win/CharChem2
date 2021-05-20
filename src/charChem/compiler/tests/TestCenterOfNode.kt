package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemExpr
import charChem.core.ChemNode
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals

private fun getNode(expr: ChemExpr): ChemNode = expr.getAgents()[0].nodes[0]
private fun getCenterItemText(expr: ChemExpr): String = makeTextFormula(getNode(expr).getCenterItem()!!)

class TestCenterOfNode {
    @Test
    fun testSingleItem() {
        val expr = compile("Mg")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "Mg")
    }
    @Test
    fun testHydrogenAndDefault() {
        val expr = compile("HO3S")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "O3")
    }
    @Test
    fun testWithCarbon() {
        val expr = compile("HOC2C3")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "C2")
    }
    @Test
    fun testWithExplicit() {
        val expr = compile("HOC2`C3")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "C3")
    }
    @Test
    fun testWithRadical() {
        val expr = compile("HEtOH")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "Et")
    }
    @Test
    fun testWithAbstract() {
        val expr = compile("H{R}O")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "O")
    }
    @Test
    fun testCommentAndAbstract() {
        val expr1 = compile("{R}\"|v\"")
        assertEquals(expr1.getMessage(), "")
        assertEquals(getCenterItemText(expr1), "R")

        val expr2 = compile("\"|v\"{R}")
        assertEquals(expr2.getMessage(), "")
        assertEquals(getCenterItemText(expr2), "R")

        val expr3 = compile("{R}`\"|v\"")
        assertEquals(expr3.getMessage(), "")
        assertEquals(getCenterItemText(expr3), "↓")
    }
    @Test
    fun testAutoNode() {
        val expr = compile("/\\")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCenterItemText(expr), "C")
    }
}