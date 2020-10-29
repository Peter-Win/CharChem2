package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.scanOxidation
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TestScanOxidation {
    @Test
    fun testNoOxidation() {
        val compiler = ChemCompiler("HNO3")
        compiler.prepareText()
        compiler.pos++
        assertEquals(compiler.curChar(), 'N')
        val charge = scanOxidation(compiler)
        assertNull(charge)
        assertEquals(compiler.curChar(), 'N')
    }
    @Test
    fun testSuccess() {
        val compiler = ChemCompiler("H(+1)N(+5)O(-2)3")
        compiler.prepareText()
        compiler.pos++
        assertEquals(compiler.curChar(), '(')
        val charge = scanOxidation(compiler)
        assertNotNull(charge)
        assertEquals(charge.value, 1.0)
        assertEquals(compiler.curChar(), 'N')
    }
    @Test
    fun testAnotherBracket() {
        val compiler = ChemCompiler("Ca(OH)2")
        compiler.prepareText()
        compiler.pos += 2
        assertEquals(compiler.curChar(), '(')
        val charge = scanOxidation(compiler)
        assertNull(charge)
        assertEquals(compiler.curChar(), '(')
    }

    @Test
    fun testError() {
        val compiler = ChemCompiler("H(+1")
        compiler.prepareText()
        compiler.pos++
        assertEquals(compiler.curChar(), '(')
        val err = assertFails { scanOxidation(compiler) }
        Lang.curLang = "ru"
        assertEquals(err.message, "Необходимо закрыть скобку, открытую в позиции 2")
    }
}