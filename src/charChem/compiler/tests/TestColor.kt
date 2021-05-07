package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestColor {
    @Test
    fun testColor() {
        val expr = compile("\$color(red)H2\$color(blue)S\$color()O4")
        assertEquals(expr.getMessage(), "")
        val items = expr.getAgents()[0].nodes[0].items
        assertEquals(items.size, 3)
        assertEquals(items[0].color, "red")
        assertEquals(items[1].color, "blue")
        assertNull(items[2].color)
        assertEquals(makeTextFormula(expr, rulesHtml),
                """<span style="color:red">H<sub>2</sub></span><span style="color:blue">S</span>O<sub>4</sub>""")
    }
}