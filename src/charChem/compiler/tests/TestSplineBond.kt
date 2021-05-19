package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

// Основное отличие _o от _s: o рассчитано на правильные многоугольники и всегда рисуется ровным кругом
// s может быть использовано для "сплющенных" циклических структур.

class TestSplineBond {
    @Test
    fun testSplineBondWithoutParams() {
        // _s without parameters equals to _o
        val expr = compile("/\\|`/`\\`|_s")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C6H6")
    }
    @Test
    fun testSplineBondWithEmptyParamsList() {
        //    _____
        //   /     \___
        //   \_____/
        val expr = compile("_(x2)\\<->`/_(x-2)`\\/_s()")
        assertEquals(expr.getMessage(), "")
        val p1 = Point(2.0, 0.0)
        val p2 = p1 + pointFromDeg(60.0)
        val p3 = p2 + Point(1.0, 0.0)
        val p4 = p2 + pointFromDeg(120.0)
        val p5 = p4 - p1
        val p6 = p5 + pointFromDeg(-120.0)
        assertEquals(expr.getAgents()[0].nodes.map { it.pt }, listOf(Point(), p1, p2, p3, p4, p5, p6))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C7H8")
        val s = expr.getAgents()[0].bonds.last()
        assertTrue(s.isCycle)
        assertEquals(s.nodes.map { it?.index }, listOf(0, 1, 2, 4, 5, 6))
    }

    @Test
    fun testDashedStyle() {
        //    /1\ /3
        //   0   2
        //   | O |
        //   6   4
        //    \5/
        val expr = compile("/\\</>|`/`\\`|_s(S:)")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C7H8")
        val s = expr.getAgents()[0].bonds.last()
        assertEquals(s.style, ":")
        assertEquals(s.nodes.map { it?.index }, listOf(0, 1, 2, 4, 5, 6))
        assertTrue(s.isCycle)
    }

    @Test
    fun testNonCycle() {
        // 0___1
        //     \2___3
        // 5___/4
        val expr = compile("-\\<->`/`-_s(S:)")
        assertEquals(expr.getMessage(), "")
        val s = expr.getAgents()[0].bonds.last()
        assertEquals(s.nodes.map { it?.index }, listOf(0, 1, 2, 4, 5))
        assertFalse(s.isCycle)
    }
    @Test
    fun testCycle() {
        // 0___1
        //     \2___3
        // 5___/4
        val expr = compile("-\\<->`/`-_s(S:,o)")
        assertEquals(expr.getMessage(), "")
        val s = expr.getAgents()[0].bonds.last()
        assertEquals(s.nodes.map { it?.index }, listOf(0, 1, 2, 4, 5))
        assertTrue(s.isCycle)
    }
    @Test
    fun testAutoCycledNodesList() {
        //      *1---2*
        //      /     \
        //     8       3
        //     |       |
        //     7       4
        //      \     /
        //      *6---5*
        val expr = compile("\$slope(45)-\\|`/`-`\\`|/_s(#1;2;5;6;1)")
        assertEquals(expr.getMessage(), "")
        val s = expr.getAgents()[0].bonds.last()
        assertTrue(s.isCycle)
        assertEquals(s.nodes.map { (it?.index ?: -1) + 1 }, listOf(1, 2, 5, 6))
    }

    @Test
    fun testNonCycledWithIntervals() {
        //  1
        // *2\__3*
        //       \4*__5
        //  *7_*6/
        //  8/
        val expr = compile("\\-\\<->`/`-_s(#2:4;6:7,S:)`/")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        val s = bonds[bonds.size - 2]
        assertEquals(s.nodes.map { (it?.index ?: -1) + 1 }, listOf(2, 3, 4, 6, 7))
        assertFalse(s.isCycle)
    }
}