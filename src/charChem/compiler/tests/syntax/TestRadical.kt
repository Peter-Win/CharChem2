package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.ChemRadical
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestRadical {
    @Test
    fun testPure() {
        val expr = ChemCompiler("EtOH").exec()
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 1)
        val node = agent.nodes[0]
        assertEquals(node.items.size, 3)
        val obj = node.items[0].obj
        assertTrue(obj is ChemRadical)
        assertEquals(obj.label, "Et")
    }
    @Test
    fun testPureCoeff() {
        val expr = ChemCompiler("Et2O").exec()
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 1)
        val node = agent.nodes[0]
        assertEquals(node.items.size, 2)
        assertEquals(node.items[0].n.num, 2.0)
        val obj = node.items[0].obj
        assertTrue(obj is ChemRadical)
        assertEquals(obj.label, "Et")
    }
    @Test
    fun testCustom() {
        val expr = ChemCompiler("{Et}OH").exec()
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 1)
        val node = agent.nodes[0]
        assertEquals(node.items.size, 3)
        val obj = node.items[0].obj
        assertTrue(obj is ChemRadical)
        assertEquals(obj.label, "Et")
    }
    @Test
    fun testCustomCoeff() {
        val expr = ChemCompiler("{Me}3P").exec()
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 1)
        val node = agent.nodes[0]
        assertEquals(node.items.size, 2)
        assertEquals(node.items[0].n.num, 3.0)
        val obj = node.items[0].obj
        assertTrue(obj is ChemRadical)
        assertEquals(obj.label, "Me")
    }
}