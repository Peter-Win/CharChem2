package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemAgent
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun getAngle(agent: ChemAgent, i: Int = 1): Int? = agent.bonds[i].dir?.polarAngleDeg()?.roundToInt()

class TestAutoCorrection {
    @Test
    fun testNotAutoNode() {
        val expr = compile("H3C--O\\CH3")
        assertEquals(expr.getMessage(), "")
        assertEquals(getAngle(expr.getAgents()[0]), 60)
    }
    @Test
    fun testHorizontalAndSlopeExternal() {
        //  __/ __  \__  __
        //        \     /
        val expr = compile("-/ -\\ `-`\\ `-`/")
        assertEquals(expr.getMessage(), "")
        val a = expr.getAgents()
        assertEquals(a.size, 4)
        assertEquals(getAngle(a[0]), -60)
        assertEquals(getAngle(a[1]),60)
        assertEquals(getAngle(a[2]), -120)
        assertEquals(getAngle(a[3]), 120)
        assertTrue(a[0].bonds[1].isCorr)
        assertTrue(a[1].bonds[1].isCorr)
        assertTrue(a[2].bonds[1].isCorr)
        assertTrue(a[3].bonds[1].isCorr)
        assertEquals(a[0].nodes[2].pt, Point(1.0, 0.0) + pointFromDeg(-60.0))
    }

    @Test
    fun testHorizontalAndSlopeInternal() {
        //
        //   _\ __  /_  __
        //       /      \
        val expr = compile("-`\\ -`/ `-/ `-\\")
        assertEquals(expr.getMessage(), "")
        val a = expr.getAgents()
        assertEquals(a.size, 4)
        assertEquals(getAngle(a[0]), -120)
        assertEquals(getAngle(a[1]),120)
        assertEquals(getAngle(a[2]), -60)
        assertEquals(getAngle(a[3]), 60)
    }

    @Test
    fun testCorrectedSlopeAndSlope() {
        // __         __
        //   \    \  /    /
        //   /  __/  \    \__
        val expr = compile("-\\`/ -/`\\ `-`/\\ `-`\\/")
        assertEquals(expr.getMessage(), "")
        val a = expr.getAgents()
        assertEquals(a.size, 4)

        assertEquals(getAngle(a[0]), 60)
        assertTrue(a[0].bonds[1].isCorr)
        assertEquals(getAngle(a[0], 2), 120)
        assertTrue(a[0].bonds[2].isCorr)

        assertEquals(getAngle(a[1]), -60)
        assertEquals(getAngle(a[1], 2), -120)
        assertTrue(a[1].bonds[1].isCorr)
        assertTrue(a[1].bonds[2].isCorr)

        assertEquals(getAngle(a[2]), 120)
        assertEquals(getAngle(a[2], 2), 60)

        assertEquals(getAngle(a[3]), -120)
        assertEquals(getAngle(a[3], 2), -60)

        val p2 = Point(1.0, 0.0) + pointFromDeg(60.0)
        assertEquals(a[0].nodes[2].pt, p2)
        assertEquals(a[0].nodes[3].pt, p2 + pointFromDeg(120.0))
    }

    @Test
    fun testTwoSlopesWithDoubleCorrection() {
        //        /  \
        //  \  /  \  /
        //  /  \
        //
        val expr = compile("\\`/ `/\\ `\\/ /`\\ ")
        assertEquals(expr.getMessage(), "")
        val a = expr.getAgents()
        assertEquals(a.size, 4)

        assertEquals(getAngle(a[0]), 120)
        assertEquals(getAngle(a[0], 0), 60)
        assertEquals(a[0].nodes[1].pt, pointFromDeg(60.0))
        assertEquals(a[0].nodes[2].pt, pointFromDeg(60.0) + pointFromDeg(120.0))

        assertEquals(getAngle(a[1], 0), 120)
        assertEquals(getAngle(a[1]), 60)
        assertEquals(a[1].nodes[1].pt, pointFromDeg(120.0))
        assertEquals(a[1].nodes[2].pt, pointFromDeg(120.0) + pointFromDeg(60.0))

        assertEquals(getAngle(a[2], 0), -120)
        assertEquals(getAngle(a[2]), -60)
        assertEquals(a[2].nodes[1].pt, pointFromDeg(-120.0))
        assertEquals(a[2].nodes[2].pt, pointFromDeg(-120.0) + pointFromDeg(-60.0))

        assertEquals(getAngle(a[3], 0), -60)
        assertEquals(getAngle(a[3]), -120)
        assertEquals(a[3].nodes[1].pt, pointFromDeg(-60.0))
        assertEquals(a[3].nodes[2].pt, pointFromDeg(-60.0) + pointFromDeg(-120.0))
    }

    @Test
    fun testPrevSlopeWithHorizontal() {
        //           o      o              o o
        //    __  __  \__  /_  __   __  __/  _\
        //   /    \              \   /
        //  o      o              o o
        val expr = compile("/- `\\- \\- `/- `\\`- /`- `/`- \\`-")
        assertEquals(expr.getMessage(), "")
        val a = expr.getAgents()
        assertEquals(a.size, 8)

        assertEquals(getAngle(a[0], 0), -60)
        assertEquals(getAngle(a[1], 0), -120)
        assertEquals(getAngle(a[2], 0), 60)
        assertEquals(getAngle(a[3], 0), 120)
        assertEquals(getAngle(a[4], 0), -120)
        assertEquals(getAngle(a[5], 0), -60)
        assertEquals(getAngle(a[6], 0), 120)
        assertEquals(getAngle(a[7], 0), 60)

        assertEquals(a[0].nodes[1].pt, pointFromDeg(-60.0))
        assertEquals(a[0].nodes[2].pt, pointFromDeg(-60.0) + Point(1.0, 0.0))
        assertEquals(a[1].nodes[1].pt, pointFromDeg(-120.0))
        assertEquals(a[1].nodes[2].pt, pointFromDeg(-120.0) + Point(1.0, 0.0))
        assertEquals(a[2].nodes[1].pt, pointFromDeg(60.0))
        assertEquals(a[2].nodes[2].pt, pointFromDeg(60.0) + Point(1.0, 0.0))
        assertEquals(a[3].nodes[1].pt, pointFromDeg(120.0))
        assertEquals(a[3].nodes[2].pt, pointFromDeg(120.0) + Point(1.0, 0.0))
        assertEquals(a[4].nodes[1].pt, pointFromDeg(-120.0))
        assertEquals(a[4].nodes[2].pt, pointFromDeg(-120.0) - Point(1.0, 0.0))
        assertEquals(a[5].nodes[1].pt, pointFromDeg(-60.0))
        assertEquals(a[5].nodes[2].pt, pointFromDeg(-60.0) - Point(1.0, 0.0))
        assertEquals(a[6].nodes[1].pt, pointFromDeg(120.0))
        assertEquals(a[6].nodes[2].pt, pointFromDeg(120.0) - Point(1.0, 0.0))
        assertEquals(a[7].nodes[1].pt, pointFromDeg(60.0))
        assertEquals(a[7].nodes[2].pt, pointFromDeg(60.0) - Point(1.0, 0.0))
    }

    @Test
    fun testNodeCoordinates() {
        // 0\
        //  1\_____2
        //   /
        // 3/
        val expr = compile("\\<->`/")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { it.dir!!.polarAngleDeg().roundToInt() }, listOf(60, 0, 120))
        assertEquals(agent.nodes.map { it.pt }, listOf(
                Point(),
                pointFromDeg(60.0),
                pointFromDeg(60.0) + Point(1.0, 0.0),
                pointFromDeg(60.0) + pointFromDeg(120.0),
        ))
    }

    @Test
    fun testPreventAutoCorrection() {
        val expr = compile("\$slope(30)-/")
        assertEquals(expr.getMessage(), "")
        assertEquals(getAngle(expr.getAgents()[0]), -30)
    }

    @Test
    fun testCoordinatesCorrection() {
        //  60°       60°60°
        // __/⸌30° -> __/\__
        //     |         |
        val expr = compile("-/\\<|>-")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        val q32 = sqrt(3.0) /2.0
        assertEquals(agent.nodes.map { it.pt }, listOf(
                Point(), Point(1.0, 0.0), Point(1.5, -q32), Point(2.0, 0.0),
                Point(2.0, 1.0), Point(3.0, 0.0)
        ))
    }

    @Test
    fun testCoordinatesCorrection2() {
        //      __       __
        // 30°⸌/60° -> \/
        //             /
        //
        val expr = compile("\\<_(A-60)->`/")
        assertEquals(expr.getMessage(), "")
        val q32 = sqrt(3.0) /2.0
        assertEquals(expr.getAgents()[0].nodes.map { it.pt }, listOf(
                Point(), Point(0.5, q32), Point(1.0, 0.0), Point(2.0, 0.0),
                Point(0.0, 2*q32)
        ))
    }

    // TODO: Надо проверить, что другие типы связей не приводят к коррекции
    @Test
    fun testNoCorrectionIV() {
        val expr = compile("|\\/")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { it.dir!!.polarAngleDeg().roundToInt() }, listOf(90, 30, -30))
        val q32 = sqrt(3.0) / 2.0
        assertEquals(agent.nodes.map { it.pt }, listOf(
                Point(), Point(0.0, 1.0), Point(q32, 1.5), Point(q32*2, 1.0)
        ))
    }
}