package charChem.compiler.tests

import charChem.compiler.convertComment
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestConvertComment {
    @Test
    fun testFastChars() {
        assertEquals(convertComment("H2|^"), "H2↑")
        assertEquals(convertComment("ArrowUp"), "↑")
        assertEquals(convertComment("BaSO4|v"), "BaSO4↓")
        assertEquals(convertComment("BaSO4ArrowDown"), "BaSO4↓")
        assertEquals(convertComment("20^oC"), "20°C")
        assertEquals(convertComment("|^ArrowUp|vArrowDown^o"), "↑↑↓↓°")
    }
    @Test
    fun testBrackets() {
        assertEquals(convertComment("[Epsilon][gamma][omicron]"), "Εγο")
        assertEquals(convertComment("[Hello]"), "[Hello]")
        assertEquals(convertComment("[[Delta]]"), "[Δ]")
    }
    @Test
    fun testTranslate() {
        Lang.curLang = "en"
        assertEquals(convertComment("H = `H`"), "H = Hydrogen")
        Lang.curLang = "ru"
        assertEquals(convertComment("H = `H`"), "H = Водород")
        assertEquals(convertComment("`HHH`"), "HHH")
    }
}