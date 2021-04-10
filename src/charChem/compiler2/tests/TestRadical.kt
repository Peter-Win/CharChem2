package charChem.compiler2.tests

import charChem.compiler2.compile
import charChem.core.PeriodicTable
import charChem.inspectors.calcMass
import charChem.inspectors.isAbstract
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TestRadical {
    @Test
    fun testMe() {
        val expr = compile("Me")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "Me")
        assertEquals(calcMass(expr), PeriodicTable.C.mass + PeriodicTable.H.mass * 3)
    }
    @Test
    fun testEt2O() {
        val expr = compile("Et2O")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "Et2O")
        val m = (PeriodicTable.C.mass * 2 + PeriodicTable.H.mass * 5) * 2 + PeriodicTable.O.mass
        assertEquals(calcMass(expr), m)
    }
    @Test
    fun testWithBrackets() {
        val expr = compile("{i-Bu}OH")
        assertEquals(expr.getMessage(), "")
        assertFalse(isAbstract(expr))
        assertEquals(makeTextFormula(expr), "i-BuOH")
        val m = PeriodicTable.C.mass * 4 + PeriodicTable.H.mass * 10 + PeriodicTable.O.mass
        assertEquals(calcMass(expr), m)
    }
}