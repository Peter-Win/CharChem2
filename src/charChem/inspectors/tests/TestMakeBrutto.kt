package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.core.ChemNode
import charChem.core.ChemObj
import charChem.inspectors.calcCharge
import charChem.inspectors.calcMass
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesCharChem
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

private fun nodeCvt(node: ChemNode): ChemObj = if (node.autoMode) makeBrutto(node) else node

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

    @Test
    fun testIgnoreOfEmptyNode() {
        val expr = compile("H3C-{}|OH")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr), rulesCharChem), "CH4O")
    }

    @Test
    fun testComments() {
        val expr = compile("//<`|0\"(E)\">\\")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr), rulesCharChem), "C3H6")
        assertEquals(expr.getAgents()[0].nodes.map { makeTextFormula(nodeCvt(it), rulesCharChem) },
                listOf("CH2", "CH", "\"(E)\"", "CH3"))
    }
    @Test
    fun testComments2() {
        val expr = compile("N//`|`\\\\`/||\\\$L(.6)|0\"1\"; \$L(.4)#2\\0\"2\"; #3/0\"3\"; #4`|0\"4\"; #5`\\0\"5\"; #6`/0\"6\"")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { it.n.roundToInt() }, listOf(2, 1, 2, 1, 2, 1, 0, 0, 0, 0, 0, 0))
        assertEquals(agent.nodes.map { makeTextFormula(nodeCvt(it), rulesCharChem) },
                listOf("N", "CH", "CH", "CH", "CH", "CH", "\"1\"", "\"2\"", "\"3\"", "\"4\"", "\"5\"", "\"6\""))
        assertEquals(makeTextFormula(makeBrutto(expr), rulesCharChem), "C5H5N")
    }
}