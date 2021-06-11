package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.PeriodicTable
import charChem.inspectors.calcMass
import charChem.inspectors.isAbstract
import charChem.inspectors.makeBrutto
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
    @Test
    fun testAcetylCoA() {
        // https://en.wikipedia.org/wiki/Acetyl-CoA
        val expr = compile("{Ac}S{CoA}")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C23H38N7O17P3S")
        assertEquals(makeTextFormula(makeBrutto(compile("{CoA}SH"))), "C21H36N7O16P3S")
    }
}