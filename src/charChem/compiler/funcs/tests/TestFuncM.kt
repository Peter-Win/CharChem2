package charChem.compiler.funcs.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.funcs.funcM
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestFuncM {
    @Test
    fun testNorm() {
        val c = ChemCompiler("238")
        c.prepareText()
        funcM(c, listOf(c.text), listOf(0))
        assertEquals(c.specMass, 238.0)
    }
}