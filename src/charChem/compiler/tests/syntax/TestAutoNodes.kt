package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesText
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestAutoNodes {
    @Test
    fun testPropene() {
        val expr = ChemCompiler("-=").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr, rulesText), "CH3-CH=CH2")
    }
    @Test
    fun testPropyne() {
        val expr = ChemCompiler("-%").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr, rulesText), "CH3-C≡CH")
    }
    @Test
    fun testAcetonitrile() {
        val expr = ChemCompiler("N%-").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr, rulesText), "N≡C-CH3")
    }
}