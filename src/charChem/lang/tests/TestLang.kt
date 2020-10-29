package charChem.lang.tests

import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestLang {
    @Test
    fun testTr() {
        val key = "Expected [must] instead of [have]"
        val params = listOf("must" to 1, "have" to 22, "pos" to 5)
        Lang.curLang = "en"
        assertEquals(Lang.tr(key, params), "Expected 1 instead of 22")
        Lang.curLang = "ru"
        assertEquals(Lang.tr(key, params), "Требуется 1 вместо 22 в позиции 5")
        assertEquals(Lang.tr(key, params, "en"), "Expected 1 instead of 22")
        assertEquals(Lang.tr(key, params, "?"), "Expected 1 instead of 22")
        assertEquals(Lang.tr(key, params, "ru-RU"), "Требуется 1 вместо 22 в позиции 5")
    }
}