package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.core.*
import org.testng.annotations.Test
import kotlin.test.*

class TestChemCompiler {
    @Test
    fun testSkipSpace() {
        val compiler = ChemCompiler(" \t\n H  O")
        compiler.prepareText()
        assertEquals(compiler.text, " \t\n H  O ")
        assertEquals(compiler.skipSpace(), 'H')
        assertEquals(compiler.skipSpace(), 'H')
        compiler.pos++
        assertEquals(compiler.skipSpace(), 'O')
    }
}