package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestNodeCharge {
    @Test
    fun testPotassiumFerrate() {
        //    +- 2 O    -+ 2-
        //    |  1 ||    |
        // K+.|....Fe....|..K+
        // 0  |  //||\\  |  6
        //    | O   O  O |
        //    +-3   5  4-+
        val expr = compile("\$ver(1.0)K^+_(x2,N0)[Fe<_(A-90,S:|)O><_(A150,S|:)O><_(A15,S|:)O><_(A70,S|:)O>]^2-_(x2,N0,T0)K^+")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { "${it.nodes[0]?.index}:${it.nodes[1]?.index}" },
                listOf("0:1", "1:2", "1:3", "1:4", "1:5", "1:6"))
        assertEquals(agent.nodes.map { makeTextFormula(makeBrutto(it)) },
                listOf("K+", "Fe", "O", "O", "O", "O", "K+"))
        assertEquals(makeTextFormula(makeBrutto(expr)), "FeK2O4")
    }
}