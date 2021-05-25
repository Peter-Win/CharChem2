package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.calcCharge
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestCalcCharge {
    @Test
    fun testSingle() {
        val expr = compile("NH4^+")
        assertEquals(expr.getMessage(), "")
        assertEquals(calcCharge(expr), 1.0)
        assertEquals(makeTextFormula(expr, rulesHtml), "NH<sub>4</sub><sup>+</sup>")
    }
    @Test
    fun testExpr() {
        val expr = compile("2Na^+ + SO4^2-")
        assertEquals(expr.getMessage(), "")
        val agents = expr.getAgents()
        assertEquals(agents.size, 2)
        assertEquals(calcCharge(agents[0]), 2.0)
        assertEquals(calcCharge(agents[1]), -2.0)
        assertEquals(calcCharge(agents[0].nodes[0]), 1.0)
        assertEquals(calcCharge(agents[1].nodes[0]), -2.0)

        assertEquals(calcCharge(expr), 0.0)
    }
    @Test
    fun testAbstract() {
        val expr = compile("'n'H^+ + O^--")
        assertEquals(expr.getMessage(), "")
        val agents = expr.getAgents()
        assertEquals(agents.size, 2)
        assertEquals(calcCharge(agents[0]), Double.NaN)
        assertEquals(calcCharge(agents[1]), -2.0)

        assertEquals(calcCharge(expr), Double.NaN)
    }
    @Test
    fun testSpecialChar() {
        val expr = compile("ClO2^\u2212")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes[0].charge!!.text, "-")
        assertEquals(calcCharge(expr), -1.0)
    }
}