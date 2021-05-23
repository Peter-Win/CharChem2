package charChem.compiler.tests

import charChem.compiler.compile
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TextOxidationState {
    @Test
    fun testNitricAcid() {
        val expr = compile("H(+)N(+5)O(-2)3")
        assertEquals(expr.getMessage(), "")
        val items = expr.getAgents()[0].nodes[0].items
        assertEquals(items.size, 3)
        assertEquals(items[0].charge?.text, "+")
        assertEquals(items[1].charge?.text, "+5")
        assertEquals(items[2].charge?.text, "-2")
    }
}