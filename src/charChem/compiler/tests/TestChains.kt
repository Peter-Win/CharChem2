package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.test.*

class TestChains {
    @Test
    fun testTwoSubChains() {
        val expr = compile("H3C-OH")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 2)
        assertEquals(agent.bonds.size, 1)
        assertTrue(agent.bonds[0].soft)
        val (left, right) = agent.nodes
        assertEquals(left.chain, right.chain)
        assertNotEquals(left.chain, 0)
        assertNotEquals(left.subChain, 0)
        assertNotEquals(left.subChain, right.subChain)
        assertEquals(left.pt, Point())
        assertEquals(right.pt, Point())
    }
    @Test
    fun testSingleSubChainForPseudoSoftBonds() {
        val expr = compile("`-`-")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 3)
        assertEquals(agent.bonds.size, 2)
        assertFalse(agent.bonds[0].soft)
        assertFalse(agent.bonds[1].soft)
        val (a, b, c) = agent.nodes
        assertNotEquals(a.chain, 0)
        assertNotEquals(a.subChain, 0)
        assertEquals(a.chain, b.chain)
        assertEquals(b.chain, c.chain)
        assertEquals(a.subChain, b.subChain)
        assertEquals(b.subChain, c.subChain)
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point(-1.0, 0.0))
        assertEquals(c.pt, Point(-2.0, 0.0))
    }

    @Test
    fun testDifferentChains() {
        val expr = compile("H-Cl; H2O")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 3)
        assertEquals(agent.bonds.size, 1)
        val (a, b, c) = agent.nodes
        assertEquals(a.chain, b.chain)
        assertNotEquals(b.chain, c.chain)
        assertNotEquals(a.subChain, b.subChain)
        assertNotEquals(b.subChain, c.subChain)
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point())
        assertEquals(c.pt, Point())
    }

    @Test
    fun testChainsMerge() {
        //      d
        //      |
        //  a---b---c
        //      |
        //      e
        val expr = compile("H-C-H; H|#2|H")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 5)
        val (a, b, c, d, e) = agent.nodes
        assertEquals(a.chain, b.chain)
        assertEquals(b.chain, c.chain)
        assertEquals(c.chain, d.chain)
        assertEquals(d.chain, e.chain)
        assertNotEquals(a.subChain, b.subChain)
        assertNotEquals(b.subChain, c.subChain)
        assertEquals(b.subChain, d.subChain)
        assertEquals(b.subChain, e.subChain)
    }
    // a---b
    //     |
    // d---c
    @Test
    fun testMergeSubChainsFromSameChain() {
        val expr = compile("O-Ca|Mg`-S`|#1")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "CaMgOS")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 4)
        val (a, b, c, d) = agent.nodes
        assertNotEquals(a.subChain, b.subChain)
        assertEquals(b.subChain, c.subChain)
        assertNotEquals(c.subChain, d.subChain)
        assertEquals(d.subChain, a.subChain)
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point())
        assertEquals(c.pt, Point(0.0, 1.0))
        assertEquals(d.pt, Point(0.0, 1.0))
    }

    // a<--d
    // |   |
    // b---c
    @Test
    fun testMergeSubChainsWithSoftBond() {
        val expr = compile("O|S-Ca`|Mg`-#1")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "CaMgOS")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 4)
        val (a, b, c, d) = agent.nodes
        assertEquals(a.chain, b.chain)
        assertEquals(b.chain, c.chain)
        assertEquals(c.chain, d.chain)
        assertEquals(d.chain, a.chain)
        assertEquals(a.subChain, b.subChain)
        assertNotEquals(b.subChain, c.subChain)
        assertEquals(c.subChain, d.subChain)
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point(0.0, 1.0))
        assertEquals(c.pt, Point())
        assertEquals(d.pt, Point(0.0, -1.0))
    }
    @Test
    fun testEthanVertical() {
        // Сложный случай. В версии 0.8 ошибка в отрисовке связи 4-5
        // В версии 1.0 ошибка в отрисовке связи 2|5
        //     7           6       y-2
        //     |           |
        // 1---2---3   0---1---2   y-1
        //     |           |
        // 4---5---6   3---4---5   y0
        //     |           |
        //     8           7       y1
        val expr = compile("H-C-H; H-C-H; H|#2|#5|H")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C2H6")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 8)
        assertEquals(agent.bonds.size, 7)
        val n = agent.nodes
        assertEquals(n[0].chain, n[1].chain)
        assertEquals(n[1].chain, n[2].chain)
        assertEquals(n[2].chain, n[3].chain)
        assertEquals(n[3].chain, n[4].chain)
        assertEquals(n[4].chain, n[5].chain)
        assertEquals(n[5].chain, n[6].chain)
        assertEquals(n[6].chain, n[7].chain)
        assertNotEquals(n[0].subChain, n[1].subChain)
        assertNotEquals(n[1].subChain, n[2].subChain)
        assertNotEquals(n[2].subChain, n[3].subChain)
        assertNotEquals(n[3].subChain, n[4].subChain)
        assertNotEquals(n[4].subChain, n[5].subChain)
        assertNotEquals(n[5].subChain, n[6].subChain)
        assertEquals(n[6].subChain, n[1].subChain)
        assertEquals(n[1].subChain, n[4].subChain)
        assertEquals(n[4].subChain, n[7].subChain)
        assertEquals(n[0].pt, Point())
        assertEquals(n[1].pt, Point(0.0, -1.0))
        assertEquals(n[2].pt, Point())
        assertEquals(n[3].pt, Point())
        assertEquals(n[4].pt, Point(0.0, 0.0))
        assertEquals(n[5].pt, Point())
        assertEquals(n[6].pt, Point(0.0, -2.0))
        assertEquals(n[7].pt, Point(0.0, 1.0))
    }
}