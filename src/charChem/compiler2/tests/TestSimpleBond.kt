package charChem.compiler2.tests

import charChem.compiler2.compile
import charChem.core.PeriodicTable
import charChem.inspectors.calcMass
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestSimpleBond {
    @Test
    fun testSimple() {
        val expr = compile("H3C-CH3")
        assertEquals(expr.getMessage(), "")
        assertEquals(calcMass(expr), PeriodicTable.C.mass * 2 + PeriodicTable.H.mass * 6)
        assertEquals(expr.entities.size, 1)
        val agents = expr.getAgents()
        assertEquals(agents.size, 1)
        val agent = agents[0]
        assertEquals(agent.nodes.size, 2)
        val (node0, node1) = agent.nodes
        assertEquals(makeTextFormula(node0), "H3C")
        assertEquals(makeTextFormula(node1), "CH3")
        assertEquals(node0.bonds.size, 1)
        assertEquals(node1.bonds.size, 1)

        assertEquals(agent.bonds.size, 1)
        val bond = agent.bonds[0]
        assertEquals(bond.nodes.size, 2)
        assertEquals(bond.nodes[0], node0)
        assertEquals(bond.nodes[1], node1)
        assertTrue(bond.soft)

        assertEquals(makeTextFormula(expr), "H3C-CH3")
    }
    @Test
    fun testAutoNodes() {
        val expr = compile("""//\""") // propen = C3H6
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), """//\""")
        val agent = expr.getAgents()[0]
        assertNotNull(agent)
        assertEquals(agent.nodes.size, 3)
        assertEquals(agent.bonds.size, 2)
        assertEquals(calcMass(expr), PeriodicTable.C.mass * 3 + PeriodicTable.H.mass * 6)
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H6")
        assertEquals(agent.bonds[0].soft, false)
        assertEquals(agent.bonds[1].soft, false)
    }
    @Test
    fun testSoftCorrection() {
        val expr = compile("-") // Ethane, C2H6
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C2H6")
        val agent = expr.getAgents()[0]
        assertNotNull(agent)
        assertEquals(agent.bonds.size, 1)
        val bond = agent.bonds[0]
        assertEquals(bond.tx, "-")
        assertFalse(bond.soft)
        assertEquals(bond.dir, Point(1.0, 0.0))
    }

    @Test
    fun testCycle() {
        val expr = compile("-|`-`|") // Cyclobutane C4H8
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertNotNull(agent)
        assertEquals(agent.nodes.size, 4)
        assertEquals(agent.bonds.size, 4)
        assertEquals(agent.nodes[0].pt, Point(0.0, 0.0))
        assertEquals(agent.nodes[1].pt, Point(1.0, 0.0))
        assertEquals(agent.nodes[2].pt, Point(1.0, 1.0))
        assertEquals(agent.nodes[3].pt, Point(0.0, 1.0))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H8")
    }

    @Test
    fun testCycleWithSoftEnd() {
        val expr = compile("|`-`|-") // last soft bond transformed into hard
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H8")
    }

    @Test
    fun testContinuationCycle() {
        // 4---0---3
        //     |   |
        //     1---2
        val expr = compile("|-`|`-`-")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertNotNull(agent)
        assertEquals(agent.nodes.size, 5)
        assertEquals(agent.nodes[4].pt, Point(-1.0, 0.0))
    }

    @Test
    fun testBondMerging() {
        // 0===1
        // |   |
        // 3---2
        val expr = compile("-|`-`|-")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 4)
        assertEquals(agent.bonds.size, 4)
        assertEquals(agent.bonds[0].n, 2.0)
    }
    @Test
    fun testBondMergingWithDifferentDirection() {
        // 0===1<--4
        //     |   |
        //     2-->3
        val expr = compile("-|-`|`-`-")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 5)
        assertEquals(agent.bonds.size, 5)
        assertEquals(agent.bonds[0].n, 2.0)
        assertEquals(agent.nodes[0].bonds.size, 1)
        assertTrue(agent.bonds[0] in agent.nodes[0].bonds)
        assertEquals(agent.nodes[1].bonds.size, 3)
        assertTrue(agent.bonds[0] in agent.nodes[1].bonds)
    }

    @Test
    fun testChainBreak() {
        val expr = compile("H3C; OH")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "CH4O")
    }
}