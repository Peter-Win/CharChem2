package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.isTextFormula
import org.testng.annotations.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestIsTextFormula {
    @Test
    fun testText() {
        assertTrue(isTextFormula(compile("H2SO4")))
        assertTrue(isTextFormula(compile("K3[Fe(CN)6]")))
        assertTrue(isTextFormula(compile("Ca^2+ + PO4^2- -> Ca3(PO4)2\"|v\"")))
        assertTrue(isTextFormula(compile("CuSO4*5H2O")))
        assertTrue(isTextFormula(compile("[2Fe2O3*3H2O]")))
    }
    @Test
    fun testBonds() {
        val expr1 = compile("H2C=CH-C%N")
        val bonds1 = expr1.getAgents()[0].bonds
        assertTrue(bonds1[0].isText)
        assertTrue(bonds1[1].isText)
        assertTrue(isTextFormula(expr1))

        assertTrue(isTextFormula(compile("CH3`-CH2`-HO")))
        assertFalse(isTextFormula(compile("-/")))
        assertFalse(isTextFormula(compile("-|")))
        assertFalse(isTextFormula(compile("//\\")))
    }
}