package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.isAbstract
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestComma {
    @Test
    fun testSimple() {
        val expr = compile("(Ni,Co,Fe)As'x'")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "(Ni,Co,Fe)Asx")
        assertTrue(isAbstract(expr))
    }
}