package charChem.compiler2.tests

import charChem.compiler2.compile
import charChem.core.PeriodicTable
import charChem.inspectors.calcMass
import charChem.inspectors.isAbstract
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestItem {
    @Test
    fun testH() {
        val h = compile("H")
        assertTrue(h.isOk())
        assertEquals(h.entities.size, 1)
        assertEquals(makeTextFormula(h), "H")
        assertEquals(calcMass(h), PeriodicTable.H.mass)
    }
    @Test
    fun testD() {
        val d = compile("D")
        assertTrue(d.isOk())
        assertEquals(makeTextFormula(d), "D")
        assertEquals(calcMass(d), PeriodicTable.D.mass)
    }
    @Test
    fun testAl() {
        val al = compile("Al")
        assertTrue(al.isOk())
        assertEquals(makeTextFormula(al), "Al")
        assertEquals(calcMass(al), PeriodicTable.Al.mass)
    }
    @Test
    fun testMgO() {
        val mgo = compile("MgO")
        assertTrue(mgo.isOk())
        assertEquals(makeTextFormula(mgo), "MgO")
        assertEquals(calcMass(mgo), PeriodicTable.Mg.mass + PeriodicTable.O.mass)
    }
    @Test
    fun testCl2() {
        val expr = compile("Cl2")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "Cl2")
        assertEquals(calcMass(expr), PeriodicTable.Cl.mass * 2.0)
    }

    @Test
    fun testC2H5OH() {
        val expr = compile("C2H5OH")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr, rulesHtml), "C<sub>2</sub>H<sub>5</sub>OH")
        assertEquals(calcMass(expr), PeriodicTable.C.mass * 2 + PeriodicTable.H.mass * 6 + PeriodicTable.O.mass)
    }
    @Test
    fun testAbstractItemCoeff() {
        val expr = compile("C'n'H'2n+2'")
        assertEquals(expr.getMessage(), "")
        assertTrue(isAbstract(expr))
        assertEquals(makeTextFormula(expr, rulesHtml), "C<sub>n</sub>H<sub>2n+2</sub>")
    }
    @Test
    fun testCustomItem() {
        val expr = compile("{R}OH")
        assertEquals(expr.getMessage(), "")
        assertTrue(isAbstract(expr))
        assertEquals(makeTextFormula(expr), "ROH")
    }
}