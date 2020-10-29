package charChem.inspectors.tests

import charChem.compiler.ChemCompiler
import charChem.inspectors.isAbstract
import org.testng.annotations.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestIsAbstract {
    @Test
    fun testExpr() {
        assertTrue(isAbstract(ChemCompiler("'n'H2O").exec()))
        assertTrue(isAbstract(ChemCompiler("C'n'H'2n+2'").exec()))
        // assertTrue(isAbstract(ChemCompiler("CuSO4*'n'H2O").exec()))
        // assertTrue(isAbstract(ChemCompiler("Cu[NH3]'n'").exec()))

        assertFalse(isAbstract(ChemCompiler("2H2O").exec()))
    }
}