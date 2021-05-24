package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

class TestMacro {
    @Test
    fun testMacro() {
        //      CH3
        //      |
        //      B
        //    /   \
        // H3C     CH3
        val expr = compile("B@:ray(a,m:CH3)<_(A&a)&m>@(-90)@ray(30)@ray(150,H3C)")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.src, "B<_(A-90)CH3><_(A30)CH3><_(A150)H3C> ")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.map { makeTextFormula(it) }, listOf("B", "CH3", "CH3", "H3C"))
        assertEquals(agent.bonds.map { it.dir!!.polarAngleDeg().roundToInt() }, listOf(-90, 30, 150))
    }
}