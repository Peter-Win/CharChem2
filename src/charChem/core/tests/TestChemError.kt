package charChem.core.tests

import charChem.core.ChemError
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class TestChemError {
    @Test
    fun testChemError() {
        val err = assertFails {
            throw ChemError("Unexpected '[C]'", listOf("C" to "?", "pos" to 11))
        }
        assertEquals(err.message, "Unexpected '?'")
        Lang.curLang = "ru"
        assertEquals(err.message, "Неверный символ '?' в позиции 11")
    }
}