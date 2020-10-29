package charChem.inspectors.tests

import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesHtml
import charChem.textRules.rulesText
import org.testng.annotations.Test
import kotlin.test.assertEquals

val rules = rulesHtml

class TestMakeHtmlFormula {
    @Test
    fun testSimple() {
        assertEquals(makeTextFormula("Li", rules), "Li")
        assertEquals(makeTextFormula("H2", rules), "H<sub>2</sub>")
        assertEquals(makeTextFormula("H2O", rules), "H<sub>2</sub>O")
        assertEquals(makeTextFormula("2H2O", rules), "<b>2</b>H<sub>2</sub>O")
        assertEquals(makeTextFormula("C'n'H'2n+2'", rules),
                "C<sub>n</sub>H<sub>2n+2</sub>")
        assertEquals(makeTextFormula("{R}OH", rules), "<i>R</i>OH")
        assertEquals(makeTextFormula("P{Me}3", rules), "PMe<sub>3</sub>")
        assertEquals(makeTextFormula("\"+[Delta]\"CO2\"|^\"", rules),
                "<em>+Δ</em>CO<sub>2</sub><em>↑</em>")
        assertEquals(makeTextFormula("SO4^2-", rules), "SO<sub>4</sub><sup>2-</sup>")
        assertEquals(makeTextFormula("SO4`^-2", rules), "<sup>-2</sup>SO<sub>4</sub>")
    }
    @Test
    fun testOperations() {
        assertEquals(makeTextFormula("2H2 + O2 = 2H2O", rules),
                """<b>2</b>H<sub>2</sub> <span class="echem-op">+</span> O<sub>2</sub> <span class="echem-op">=</span> <b>2</b>H<sub>2</sub>O""")
        assertEquals(makeTextFormula("""Se + O2 "250^oC"-->"NO2" SeO2""", rules),
                """Se <span class="echem-op">+</span> O<sub>2</sub> <span class="echem-op"><span class="echem-opcomment">250°C</span><span class="chem-long-arrow">→</span><span class="echem-opcomment">NO2</span></span> SeO<sub>2</sub>""")
        assertEquals(makeTextFormula("""U + C "T"-> UC""", rules),
            """U <span class="echem-op">+</span> C <span class="echem-op"><span class="echem-opcomment">T</span>→</span> UC""")
    }
    @Test
    fun testMass() {
        assertEquals(makeTextFormula("\$M(235)U", rules), """<sup>235</sup>U""")
        assertEquals(makeTextFormula("\$nM(238)U", rules),
        """<span class="echem-mass-and-num">238<br/>92</span>U""")
        assertEquals(makeTextFormula("\$nM(238)UF6", rules),
                """<span class="echem-mass-and-num">238<br/>92</span>UF<sub>6</sub>""")
        assertEquals(makeTextFormula("\$nM(1,0){n}", rules),
        """<span class="echem-mass-and-num">1<br/>0</span><i>n</i>""")
    }
}