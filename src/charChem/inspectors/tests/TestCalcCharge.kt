package charChem.inspectors.tests

import charChem.compiler.ChemCompiler
import charChem.inspectors.calcCharge
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestCalcCharge {
    @Test
    fun testSingle() {
        val expr = ChemCompiler("NH4^+").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(calcCharge(expr), 1.0)
    }
    @Test
    fun testExpr() {
        val expr = ChemCompiler("2Na^+ + SO4^2-").exec()
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
        val expr = ChemCompiler("'n'H^+ + O^--").exec()
        assertEquals(expr.getMessage(), "")
        val agents = expr.getAgents()
        assertEquals(agents.size, 2)
        assertEquals(calcCharge(agents[0]), Double.NaN)
        assertEquals(calcCharge(agents[1]), -2.0)

        assertEquals(calcCharge(expr), Double.NaN)
    }
}