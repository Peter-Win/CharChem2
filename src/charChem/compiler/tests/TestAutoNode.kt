package charChem.compiler.tests

import charChem.compiler.compile
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestAutoNode {
    @Test
    fun testAutoNode() {
        val expr = compile("/c///")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 3)
        assertEquals(agent.bonds.map { it.linearText() }, listOf("/", "///"))
    }
}