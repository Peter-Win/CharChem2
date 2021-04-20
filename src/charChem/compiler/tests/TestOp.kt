package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestOp {
    @Test
    fun testPlus() {
        val expr = compile("+")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "+")
    }
    @Test
    fun testEq() {
        val expr = compile("Cu + O = CuO")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 5)
        assertEquals(makeTextFormula(expr), "Cu + O = CuO")
        val agents = expr.getAgents()
        assertEquals(agents.size, 3)
        assertEquals(agents[0].part, 0)
        assertEquals(agents[1].part, 0)
        assertEquals(agents[2].part, 1)
    }
    @Test
    fun testPreComm() {
        val expr = compile(" \"T\"->")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 1)
        assertEquals(makeTextFormula(expr, rulesHtml),
                """<span class="echem-op"><span class="echem-opcomment">T</span>→</span>""")
    }
    @Test
    fun testPostComm() {
        val expr = compile("->\"[Delta]\"")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 1)
        assertEquals(makeTextFormula(expr, rulesHtml),
                """<span class="echem-op">→<span class="echem-opcomment">Δ</span></span>""")
    }
    @Test
    fun testBothComm() {
        val expr = compile(""" "T"->"-[Delta]" """)
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 1)
        assertEquals(makeTextFormula(expr, rulesHtml),
                """<span class="echem-op"><span class="echem-opcomment">T</span>→<span class="echem-opcomment">-Δ</span></span>""")
    }
}