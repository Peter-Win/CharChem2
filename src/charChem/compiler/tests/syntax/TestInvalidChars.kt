package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.*
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.*

class TestInvalidChars {
    @Test
    fun testRussian() {
        val expr = ChemCompiler("H2СО3").exec()
        Lang.curLang = "ru"
        assertEquals(
                expr.getMessage(),
                "Недопустимый русский символ 'С'. Для описания химического элемента должны использоваться только латинские символы."
        )
    }

    @Test
    fun testRussianSmall() {
        val expr = ChemCompiler("Nа2SO4").exec()
        Lang.curLang = "ru"
        assertEquals(
                expr.getMessage(),
                "Недопустимый русский символ 'а'. Для описания химического элемента должны использоваться только латинские символы."
        )
    }

    @Test
    fun testGreek() {
        val expr = ChemCompiler("H3ΡO4").exec()
        Lang.curLang = "ru"
        assertEquals(
                expr.getMessage(),
                "Недопустимый символ 'Ρ'. Для описания химического элемента должны использоваться только латинские символы."
        )
    }
}