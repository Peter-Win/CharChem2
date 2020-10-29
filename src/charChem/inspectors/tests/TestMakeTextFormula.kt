package charChem.inspectors.tests

import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesText
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestMakeTextFormula {
    @Test
    fun testSimple() {
        assertEquals(makeTextFormula("Li", rulesText), "Li")
        assertEquals(makeTextFormula("H2", rulesText), "H2")
        assertEquals(makeTextFormula("H2O", rulesText), "H2O")
        assertEquals(makeTextFormula("2H2O", rulesText), "2H2O")
        assertEquals(makeTextFormula("C'n'H'2n+2'", rulesText), "CnH2n+2")
        assertEquals(makeTextFormula("{R}OH", rulesText), "ROH")
        assertEquals(makeTextFormula("P{Me}3", rulesText), "PMe3")
        assertEquals(makeTextFormula("\"+[Delta]\"CO2\"|^\"", rulesText), "+ΔCO2↑")
        assertEquals(makeTextFormula("2H2 + O2 = 2H2O", rulesText), "2H2 + O2 = 2H2O")
        assertEquals(makeTextFormula("SO4^2-", rulesText), "SO42-")
        assertEquals(makeTextFormula("SO4`^-2", rulesText), "-2SO4")
    }
}