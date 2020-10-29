package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.ChemNodeItem
import charChem.core.Visitor
import charChem.inspectors.calcMass
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestSpecMass {
    @Test
    fun testM() {
        val expr = ChemCompiler("\$M(235)U").exec()
        assertEquals(expr.getMessage(), "")
        val items = mutableListOf<ChemNodeItem>()
        expr.walk(object : Visitor() {
            override fun itemPre(obj: ChemNodeItem) {
                items.add(obj)
            }
        })
        assertEquals(items.size, 1)
        assertEquals(items[0].mass, 235.0)
        assertEquals(calcMass(expr), 235.0)
    }
}