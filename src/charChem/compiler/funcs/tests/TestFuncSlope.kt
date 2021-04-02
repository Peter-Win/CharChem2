package charChem.compiler.funcs.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.funcs.funcSlope
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestFuncSlope {
    @Test
    fun testSlope45() {
        val c = ChemCompiler("")
        assertEquals(c.userSlope, 0.0)
        funcSlope(c, listOf("45"), listOf(0))
        assertEquals(c.userSlope, Math.PI / 4)
    }
    @Test
    fun testSlopeEmpty() {
        val c = ChemCompiler("")
        c.userSlope = Math.PI / 4
        funcSlope(c, listOf(), listOf())
        assertEquals(c.userSlope, 0.0)
    }
}