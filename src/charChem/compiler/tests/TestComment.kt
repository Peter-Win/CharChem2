package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.PeriodicTable
import charChem.inspectors.calcMass
import charChem.inspectors.isAbstract
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.lang.Lang
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class TestComment {
    @Test
    fun testCommentEndSimple() {
        val expr = compile("C\"(solid)\"")
        assertEquals(expr.getMessage(), "")
        assertFalse(isAbstract(expr))
        assertEquals(calcMass(expr), PeriodicTable.C.mass)
        assertEquals(makeTextFormula(expr), "C(solid)")
        assertEquals(makeTextFormula(expr, rulesHtml), "C<em>(solid)</em>")
    }
    @Test
    fun testCommentWithTranslation() {
        val oldLang = Lang.curLang
        Lang.curLang = "ru"
        val expr = compile("NaCl\"`(aq)`\"")
        Lang.curLang = oldLang
        assertEquals(expr.getMessage(), "")
        assertFalse(isAbstract(expr))
        assertEquals(makeTextFormula(expr, rulesHtml), "NaCl<em>(р-р)</em>")
        assertEquals(calcMass(expr), PeriodicTable.Na.mass + PeriodicTable.Cl.mass)
    }
    @Test
    fun testCommentWithSpecial() {
        val expr = compile("S\"|v\"")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "S↓")
        assertEquals(calcMass(expr), PeriodicTable.S.mass)
    }
    @Test
    fun testCommentWithGreek() {
        val expr = compile("Ar\"[Theta][psi]\"")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "ArΘψ")
        assertEquals(calcMass(expr), PeriodicTable.Ar.mass)
    }
    @Test
    fun testBeforeAgent() {
        val expr = compile("\"|^\"F2")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "↑F2")
        assertEquals(calcMass(expr), PeriodicTable.F.mass * 2)
    }
    @Test
    fun testError() {
        val expr = compile("Cu\"123")
        Lang.curLang = "en"
        assertEquals(expr.getMessage(), "Comment is not closed")
        val pos = expr.error?.params?.find{it.first == "pos"}
        assertNotNull(pos)
        assertEquals(pos.second, 3)
    }
}