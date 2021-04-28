package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.calcCharge
import charChem.inspectors.calcMass
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestMakeBrutto {
    @Test
    fun testSingleNode() {
        val src = compile("CH3CH2OH")
        assertEquals(src.getMessage(), "")
        val brutto = makeBrutto(src)
        assertEquals(brutto.getMessage(), "")
        assertEquals(makeTextFormula(brutto), "C2H6O")
        assertEquals(calcMass(src), calcMass(brutto))
    }
    @Test
    fun testCharge() {
        val expr = compile("SO4^2-")
        assertEquals(expr.getMessage(), "")
        val brutto = makeBrutto(expr)
        assertEquals(brutto.getMessage(), "")
        assertEquals(calcCharge(brutto), -2.0)
        assertEquals(makeTextFormula(brutto), "O4S2-")
        assertEquals(makeTextFormula(brutto, rulesHtml), "O<sub>4</sub>S<sup>2-</sup>")
    }
    @Test
    fun testComplex() {
        val expr = compile("[Fe(CN)6]^4-")
        assertEquals(expr.getMessage(), "")
        val brutto = makeBrutto(expr)
        assertEquals(brutto.getMessage(), "")
        val agent = brutto.getAgents()[0]
        val node = agent.nodes[0]
        assertEquals(node.charge?.value, -4.0)
        assertEquals(calcCharge(brutto), -4.0)
        assertEquals(makeTextFormula(expr, rulesHtml),
                "[Fe(CN)<sub>6</sub>]<sup>4-</sup>")
        assertEquals(makeTextFormula(brutto, rulesHtml),
                "C<sub>6</sub>FeN<sub>6</sub><sup>4-</sup>")
    }
}