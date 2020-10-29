package charChem.compiler.funcs.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.funcs.funcnM
import org.testng.annotations.Test
import kotlin.test.*

class TestFuncnM () {
    @Test
    fun testShort() {
        val c = ChemCompiler("\$nM(238)U")
        c.prepareText()
        funcnM(c, listOf("238"), listOf(4))
        assertEquals(c.specMass, 238.0)
        assertEquals(c.customAtomNumber, -1)
    }
    @Test
    fun testFull() {
        val c = ChemCompiler("\$nM(1,0){n}")
        c.prepareText()
        funcnM(c, listOf("1", "0"), listOf(4, 6))
        assertEquals(c.specMass, 1.0)
        assertEquals(c.customAtomNumber, 0)
    }
}