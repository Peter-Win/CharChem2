package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.lang.Lang
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestBranch {
    @Test
    fun testSimple() {
        // a---b---d
        //     |
        //     c
        val expr = compile("-<|>-")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H10")
        val nodes = expr.getAgents()[0].nodes
        assertEquals(nodes.size, 4)
        val (a, b, c, d) = nodes
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point(1.0, 0.0))
        assertEquals(c.pt, Point(1.0, 1.0))
        assertEquals(d.pt, Point(2.0, 0.0))
    }
    @Test
    fun testNotAutoNode() {
        val expr = compile("H3C--N<|CH3>--CH3")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H9N")
        val nodes = expr.getAgents()[0].nodes
        assertEquals(nodes.size, 4)
        val (a, b, c, d) = nodes
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point(1.0, 0.0))
        assertEquals(c.pt, Point(1.0, 1.0))
        assertEquals(d.pt, Point(2.0, 0.0))
    }
    @Test
    fun testNested() {
        // N,N-Dimethylisopropylamine
        // ---N---
        //    |
        // ---+---
        val expr = compile("--N<|<`->->--")
        assertEquals(expr.getMessage(), "")
        val n = expr.getAgents()[0].nodes
        assertEquals(n.size, 6)
        assertEquals(n[0].pt, Point())
        assertEquals(n[1].pt, Point(1.0, 0.0))
        assertEquals(n[2].pt, Point(1.0, 1.0))
        assertEquals(n[3].pt, Point(0.0, 1.0))
        assertEquals(n[4].pt, Point(2.0, 1.0))
        assertEquals(n[5].pt, Point(2.0, 0.0))
    }
    @Test
    fun testSubChainInBranch() {
        // 2-Pentanol         Nodes         SubChain
        // H3C-+---           0--1--5       1--1--1
        //     |                 |             |
        //     +-CH2-CH3         2--3~~4       1--1--2
        val expr = compile("H3C-<|-CH2-CH3>-")
        assertEquals(expr.getMessage(), "")
        val n = expr.getAgents()[0].nodes
        assertEquals(n.size, 6)
        assertEquals(n[0].subChain, n[1].subChain)
        assertEquals(n[1].subChain, n[2].subChain)
        assertEquals(n[2].subChain, n[3].subChain)
        assertTrue(n[3].subChain < n[4].subChain)
        assertEquals(n[1].subChain, n[5].subChain)
        assertEquals(n[0].pt, Point())
        assertEquals(n[1].pt, Point(1.0, 0.0))
        assertEquals(n[2].pt, Point(1.0, 1.0))
        assertEquals(n[3].pt, Point(2.0, 1.0))
        assertEquals(n[4].pt, Point())
        assertEquals(n[5].pt, Point(2.0, 0.0))
    }
    @Test
    fun testNotClosed() {
        val expr = compile("</<\\ + H2O")
        Lang.curLang = "ru"
        assertEquals(expr.getMessage(), "Необходимо закрыть ветку, открытую в позиции 3")
    }
    @Test
    fun testNotOpened() {
        val expr = compile("/>")
        Lang.curLang = "ru"
        assertEquals(expr.getMessage(), "Нельзя закрыть ветку в позиции 2, которая не открыта")
    }
    @Test
    fun testAlternativeSyntax() {
        // a---b---d
        //     |
        //     c
        val expr = compile("-(*|OH*)-")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H8O")
        val nodes = expr.getAgents()[0].nodes
        assertEquals(nodes.size, 4)
        val (a, b, c, d) = nodes
        assertEquals(a.pt, Point())
        assertEquals(b.pt, Point(1.0, 0.0))
        assertEquals(c.pt, Point(1.0, 1.0))
        assertEquals(d.pt, Point(2.0, 0.0))
    }
}