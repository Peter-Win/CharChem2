package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.scanComment
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestScanComment {
    @Test
    fun testGood() {
        val c = ChemCompiler("\"Hello\"")
        c.prepareText()
        c.pos = 1
        val commText = scanComment(c)
        assertEquals(commText, "Hello")
        assertEquals(c.curChar(), '"')
    }
    @Test
    fun testNotClosed() {
        val c = ChemCompiler("\"Hello")
        c.prepareText()
        c.pos = 1
        val err = assertFails { scanComment(c) }
        Lang.curLang = "ru"
        assertEquals(err.message, "Не закрыт комментарий, начатый в позиции 1")
    }
}