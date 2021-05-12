package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

class TestPolygonalBond {
    @Test
    fun testSimpleP() {
        val expr = compile("_p_p5_pp_pO_p_p")
        // Последний сегмент накладывается на первый с образованием двойной связи
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds.map { it.dir!!.polarAngleDeg().roundToInt() },
            listOf(0, 72, 144, -144, -72))
        assertEquals(bonds.map { it.n }, listOf(2.0, 1.0, 2.0, 1.0, 1.0))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H4O")
    }
    @Test
    fun testQ3() {
        val expr = compile("-_qq3_q3")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds.map { it.dir!!.polarAngleDeg().roundToInt() },
                listOf(0, -120, 120))
        assertEquals(bonds.map { it.n }, listOf(1.0, 2.0, 1.0))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H4")
    }
    @Test
    fun testUsingLengthOfPreviousBond() {
        val expr = compile("_(x2)_p4")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].nodes.map { it.pt },
                listOf(Point(), Point(2.0, 0.0), Point(2.0, 2.0)))
    }
    @Test
    fun testSuffixesWD() {
        val expr = compile("-_p6w_p6ww_p6_p6d_p6dd")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].bonds.map { "${it.w0},${it.w1}" },
            listOf("0,0", "0,1", "1,0", "0,0", "0,-1", "-1,0"))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C6H12")
    }
    @Test
    fun testZeroBond() {
        val expr = compile("-_p4o_p4_pp4")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds.map { it.n }, listOf(1.0, 0.0, 1.0, 2.0))
    }
}