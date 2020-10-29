package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.ChemAgent
import charChem.core.ChemOp
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestEquation {
    @Test
    fun testSimple() {
        val expr = ChemCompiler("2H2 + O2 -> 2H2O").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 5)
        val (e0, e1, e2, e3, e4) = expr.entities

        assertTrue(e0 is ChemAgent)
        assertEquals(e0.part, 0)

        assertTrue(e1 is ChemOp)
        assertEquals(e1.srcText, "+")
        assertFalse(e1.eq)

        assertTrue(e2 is ChemAgent)
        assertEquals(e2.part, 0)

        assertTrue(e3 is ChemOp)
        assertEquals(e3.srcText, "->")
        assertTrue(e3.eq)

        assertTrue(e4 is ChemAgent)
        assertEquals(e4.part, 1)
    }
}