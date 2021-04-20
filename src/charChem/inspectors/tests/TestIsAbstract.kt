package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.isAbstract
import org.testng.annotations.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestIsAbstract {
    @Test
    fun testExpr() {
        assertTrue(isAbstract(compile("'n'H2O")))
        assertTrue(isAbstract(compile("C'n'H'2n+2'")))
        // assertTrue(isAbstract(ChemCompiler("CuSO4*'n'H2O").exec()))
        // assertTrue(isAbstract(ChemCompiler("Cu[NH3]'n'").exec()))

        assertFalse(isAbstract(compile("2H2O")))
    }
}