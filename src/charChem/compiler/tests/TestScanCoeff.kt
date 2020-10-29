package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.scanCoeff
import charChem.core.ChemError
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.*

class TestScanCoeff {
    @Test
    fun testNumberNull() {
        val c = ChemCompiler("A56")
        c.prepareText()
        val k = scanCoeff(c)
        assertNull(k)
    }
    @Test
    fun testNumberShort() {
        val c = ChemCompiler("2")
        c.prepareText()
        val k = scanCoeff(c)
        assertNotNull(k)
        assertTrue(k.isNumber())
        assertEquals(k.num, 2.0)
    }
    @Test
    fun testNumber123() {
        val c = ChemCompiler("123")
        c.prepareText()
        val k = scanCoeff(c)
        assertNotNull(k)
        assertTrue(k.isNumber())
        assertEquals(k.num, 123.0)
    }
    @Test
    fun testNumberMix() {
        val c = ChemCompiler("15H2O")
        c.prepareText()
        val k = scanCoeff(c)
        assertNotNull(k)
        assertTrue(k.isNumber())
        assertEquals(k.num, 15.0)
        assertEquals(c.curChar(), 'H')
        c.pos++
        val k2 = scanCoeff(c)
        assertNotNull(k2)
        assertTrue(k2.isNumber())
        assertEquals(k2.num, 2.0)
    }

    @Test
    fun testAbstract() {
        // Abstract formula of alkane
        val c = ChemCompiler("C'n'H'2n+2'")
        c.prepareText()
        c.pos++
        val k = scanCoeff(c)
        assertNotNull(k)
        assertFalse(k.isNumber())
        assertEquals(k.text, "n")
        assertEquals(c.curChar(), 'H')
        c.pos++
        val k2 = scanCoeff(c)
        assertNotNull(k2)
        assertFalse(k2.isNumber())
        assertEquals(k2.text, "2n+2")
    }
    @Test
    fun testAbstractErr() {
        val c = ChemCompiler("H'2n+2")
        c.prepareText()
        c.pos++
        val err = assertFails { scanCoeff(c) }
        Lang.curLang = "ru"
        assertEquals(err.message, "Не закрыт абстрактный коэффициент, начатый в позиции 2")
    }
}