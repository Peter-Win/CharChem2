package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemAgent
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun getAngle(agent: ChemAgent, i: Int = 1): Int? = agent.bonds[i].dir?.polarAngleDeg()?.roundToInt()

class TestAutoCorrection {
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
    fun testPreventAutoCorrection() {
        val expr = compile("\$slope(30)-/")
        assertEquals(expr.getMessage(), "")
        assertEquals(getAngle(expr.getAgents()[0]), -30)
    }

    // TODO: Надо проверить, что другие типы связей не приводят к коррекции
}