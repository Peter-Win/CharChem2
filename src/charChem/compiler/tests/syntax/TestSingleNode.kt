package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.*
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TestSingleNode {
    @Test
    fun testSingleNode() {
        val expr = ChemCompiler("Na2MoO4").exec()
        assertEquals(expr.getMessage(), "")
        val items: MutableList<ChemNodeItem> = mutableListOf()
        expr.walk(object: Visitor() {
            override fun itemPre(obj: ChemNodeItem) {
                items.add(obj)
            }
        })
        assertEquals(items.size, 3)
        assertTrue(items[0].obj is ChemAtom)
        val atom1: ChemAtom = items[0].obj as ChemAtom
        assertEquals(atom1.id, "Na")
        assertEquals(items[0].n.toString(), "2")
        assertTrue(items[1].obj is ChemAtom)
        val atom2: ChemAtom = items[1].obj as ChemAtom
        assertEquals(atom2.id, "Mo")
        assertFalse(items[1].n.isSpecified())
        assertTrue(items[2].obj is ChemAtom)
        val atom3 = items[2].obj as ChemAtom
        assertEquals(atom3.id, "O")
        assertEquals(items[2].n.num, 4.0)
    }

    @Test
    fun testAgentCoeff() {
        val expr = ChemCompiler("H2O").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 1)
        val agent: ChemAgent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 1)
        val node: ChemNode = agent.nodes[0]
        assertEquals(node.items.size, 2)
    }

    @Test
    fun testOxidation() {
        val expr = ChemCompiler("Fe(ii)S(+6)O(-2)4").exec()
        assertEquals(expr.getMessage(), "")
        val items: MutableList<ChemNodeItem> = mutableListOf()
        expr.walk(object: Visitor() {
            override fun itemPre(obj: ChemNodeItem) {
                items.add(obj)
            }
        })
        assertEquals(items.size, 3)
        assertNotNull(items[0].charge)
        assertEquals(items[0].charge!!.value, 2.0)
        assertEquals(items[0].charge!!.text, "II")
        assertNotNull(items[1].charge)
        assertEquals(items[1].charge!!.value, 6.0)
        assertEquals(items[1].charge!!.text, "+6")
        assertNotNull(items[2].charge)
        assertEquals(items[2].charge!!.value, -2.0)
        assertEquals(items[2].charge!!.text, "-2")
        assertNotNull(items[2].n)
        assertTrue(items[2].n.isNumber())
        assertEquals(items[2].n.num, 4.0)
    }

    @Test
    fun testPrefixAgentCoeff() {
        val expr = ChemCompiler("5H2").exec()
        assertEquals(expr.getMessage(), "")
        val agents: MutableList<ChemAgent> = mutableListOf()
        expr.walk(object: Visitor() {
            override fun agentPre(obj: ChemAgent) {
                agents.add(obj)
            }
        })
        assertEquals(agents.size, 1)
        assertNotNull(agents[0].n)
        assertTrue(agents[0].n.isNumber())
        assertEquals(agents[0].n.num, 5.0)
    }
    @Test
    fun testNodeCharge() {
        val expr = ChemCompiler("NH4`^+").exec()
        assertEquals(expr.getMessage(), "")
        val nodes: MutableList<ChemNode> = mutableListOf()
        expr.walk(object: Visitor() {
            override fun nodePre(obj: ChemNode) {
                nodes.add(obj)
            }
        })
        assertEquals(nodes.size, 1)
        val charge = nodes[0].charge
        assertNotNull(charge)
        assertEquals(charge.text, "+")
        assertEquals(charge.value, 1.0)
        assertTrue(charge.isLeft)
    }
}