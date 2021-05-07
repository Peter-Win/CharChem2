package charChem.compiler.main.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.funcs.*
import charChem.compiler.main.getAtomColor
import charChem.compiler.main.getItemColor
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestGetColors {
    @Test
    fun testGetItemColor() {
        val compiler = createTestCompiler("")
        assertNull(getItemColor(compiler))
        funcColor(compiler, listOf("black"), listOf(0))
        assertEquals(getItemColor(compiler), "black")
        funcItemColor1(compiler, listOf("red"), listOf(0))
        funcItemColor(compiler, listOf("green"), listOf(0))
        assertEquals(getItemColor(compiler), "red")
        assertEquals(getItemColor(compiler), "green")
        assertEquals(getItemColor(compiler), "green")
        funcItemColor(compiler, listOf(), listOf())
        assertEquals(getItemColor(compiler), "black")
    }
    @Test
    fun testGetAtomColor() {
        val compiler = createTestCompiler("")
        assertNull(getAtomColor(compiler))
        funcColor(compiler, listOf("#000"), listOf(0)) // Never used in atom color
        funcAtomColor1(compiler, listOf("orange"), listOf(0))
        funcAtomColor(compiler, listOf("magenta"), listOf(0))
        assertEquals(getAtomColor(compiler), "orange")
        assertEquals(getAtomColor(compiler), "magenta")
        assertEquals(getAtomColor(compiler), "magenta")
        funcAtomColor(compiler, listOf(), listOf())
        assertNull(getAtomColor(compiler))
    }
}