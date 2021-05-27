package charChem.compiler.tests

import charChem.compiler.compile
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

class TestBondMerge {
    @Test
    fun testKetone() {
        //    0
        //    |1
        //   /\\
        //  3  O 2
        val expr = compile("|\\O`\\`/")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map {
            "${it.nodes[0]?.index}(${it.dir!!.polarAngleDeg().roundToInt()}*${it.n.roundToInt()})${it.nodes[1]?.index}"
        }, listOf("0(90*1)1", "1(30*2)2", "1(150*1)3"))
    }
}