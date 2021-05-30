package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.math.sqrt
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
        // Все узлы в одной цепи
        assertEquals(a.chain, b.chain)
        assertEquals(b.chain, c.chain)
        assertEquals(c.chain, d.chain)
        assertEquals(d.chain, e.chain)
        // Подцепи: a, b-d-e, c
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
        val expr = compile("{A}-{B}|{C}`-{D}`|#1")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "ABCD")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 4)
        val (a, b, c, d) = agent.nodes
        // All nodes in same chain
        assertEquals(a.chain, b.chain)
        assertEquals(b.chain, c.chain)
        assertEquals(c.chain, d.chain)
        // 3 sub chains: a, b-c, d
        assertNotEquals(a.subChain, b.subChain)
        assertEquals(b.subChain, c.subChain)
        assertNotEquals(c.subChain, d.subChain)
        assertNotEquals(d.subChain, a.subChain)
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point())
        assertEquals(c.pt, Point(0.0, 1.0))
        assertEquals(d.pt, Point())
        // last bond is transition
        assertNull(agent.bonds.last().dir)
    }

    @Test
    fun testMergeSubChainsWithSoftBond() {
        // N
        // |
        // B<--K
        // |   |
        // C---F
        val expr = compile("B|C-F`|K`-#1`|N")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "CBFKN")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 5)
        val (b, c, f, k, n) = agent.nodes
        // Цепь у всех узлов одинаковая
        assertEquals(b.chain, c.chain)
        assertEquals(c.chain, f.chain)
        assertEquals(f.chain, k.chain)
        assertEquals(k.chain, n.chain)
        // Должно быть две подцепи b-c-n и f-k
        assertEquals(b.subChain, c.subChain)
        assertEquals(c.subChain, n.subChain)
        assertNotEquals(c.subChain, f.subChain)
        assertEquals(f.subChain, k.subChain)

        assertEquals(b.pt, Point())
        assertEquals(c.pt, Point(0.0, 1.0))
        assertEquals(f.pt, Point())
        assertEquals(k.pt, Point(0.0, -1.0))
        assertEquals(n.pt, Point(0.0, -1.0))
        // Связь между k и b выполняет переход от одной подцепи к другой
        val bondKB = expr.getAgents()[0].bonds[3]
        assertEquals(bondKB.nodes[0], k)
        assertEquals(bondKB.nodes[1], b)
        assertNull(bondKB.dir)
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
    @Test
    fun testCoordinates() {
        //    0  1  2  3
        // -1       2--3
        //             |
        //  0 0--------1
        val expr = compile("_(x3); -|#2")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        val nodes = agent.nodes
        assertEquals(nodes[0].subChain, nodes[2].subChain)
        assertEquals(nodes.map { it.pt }, listOf(
                Point(), Point(3.0, 0.0), Point(2.0, -1.0), Point(3.0, -1.0)
        ))
    }
}