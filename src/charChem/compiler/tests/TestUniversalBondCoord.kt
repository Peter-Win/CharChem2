package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestUniversalBondCoord {
    @Test
    fun testXY() {
        val expr = compile("_(x1)_(y-1)_(x-1,y1)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.map { it.pt },
                listOf(Point(), Point(1.0, 0.0), Point(1.0, -1.0)))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H6")
    }

    @Test
    fun testAbsoluteAngle() {
        val expr = compile("_(A0)\$L(2)_(A90)_(A180,L1)_(A-90)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(
                agent.nodes.map { it.pt },
                listOf(Point(), Point(1.0, 0.0), Point(1.0, 2.0), Point(0.0, 2.0))
        )
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H8")
    }

    @Test
    fun testRelativeAngle() {
        val expr = compile("_(a45)O_(a90)_(a-90)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        val a = Point()
        val b = a + pointFromDeg(45.0)
        val c = b + pointFromDeg(135.0)
        val d = c + pointFromDeg(45.0)
        assertEquals(agent.nodes.map { it.pt }, listOf(a, b, c, d))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H8O")
    }

    @Test
    fun testDefaultPolygonal() {
        val expr = compile("_(P)_(P)_(P)_(P)_(P)")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C5H10")
        assertEquals(expr.getAgents()[0].bonds.map { it.dir?.polarAngleDeg()?.toInt() },
                listOf(0, 72, 144, -144, -72))
    }

    @Test
    fun testNegativePolygonal() {
        val expr = compile("-_(P-4)_(P-4)_(P-4)")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H8")
        assertEquals(expr.getAgents()[0].nodes.map { it.pt },
                listOf(Point(), Point(1.0, 0.0), Point(1.0, -1.0), Point(0.0, -1.0)))
    }
    @Test
    fun testReferencesList() {
        //   \
        //     \
        //  |  /
        //  |/
        val expr = compile("_(x1,y1)_(x-1,y1)_(p1;-1)")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H10")
        assertEquals(expr.getAgents()[0].nodes.map { it.pt },
                listOf(Point(), Point(1.0, 1.0), Point(0.0, 2.0), Point(0.0, 1.0)))
    }
    @Test
    fun testEmptyRefList() {
        val expr = compile("_(p)")
        assertEquals(expr.getMessage("ru"), "Неправильная ссылка на узел '' в позиции 4")
    }
    @Test
    fun testInvalidRef() {
        val expr = compile("_(p22)")
        assertEquals(expr.getMessage("ru"), "Неправильная ссылка на узел '22' в позиции 4")
    }

    @Test
    fun testWithoutParams() {
        val expr = compile("-|_#1")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 3)
        assertEquals(agent.bonds.size, 3)
        assertEquals(agent.bonds[2].dir, Point(-1.0, -1.0))
        assertEquals(agent.bonds[2].nodes[1], agent.nodes[0])
    }

    @Test
    fun testUsingReferencesInXYParams() {
        val expr = compile("-|_(x#1)_(y#1)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.size, 4)
        assertEquals(agent.nodes.map { it.pt }, listOf(
                Point(), Point(1.0, 0.0), Point(1.0, 1.0), Point(0.0, 1.0)
        ))
    }
    @Test
    fun testUsingRefsListInXY() {
        val expr = compile("-_(x#1;-1,y1)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.size, 2)
        assertEquals(agent.nodes.map { it.pt }, listOf(
                Point(), Point(1.0, 0.0), Point(0.5, 1.0)
        ))
    }
    @Test
    fun testInvalidRefsListInXY() {
        val expr = compile("-_(x#1;-11,y1)")
        assertEquals(expr.getMessage("ru"), "Неправильная ссылка на узел '-11' в позиции 8")
    }
}