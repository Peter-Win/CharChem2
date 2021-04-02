package charChem.compiler.tests

import charChem.compiler.resolveAutoNodes
import charChem.core.ChemAgent
import charChem.core.ChemBond
import charChem.core.ChemNode
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesText
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestResolveAutoNodes {
    @Test
    fun testAutoNodes() {
        val nodeA = ChemNode()
        val nodeB = ChemNode()
        val nodeC = ChemNode()
        nodeA.autoMode = true
        nodeB.autoMode = true
        nodeC.autoMode = true
        val bond1 = ChemBond()
        val bond2 = ChemBond()
        bond2.n = 2.0
        bond1.nodes[0] = nodeA
        bond1.nodes[1] = nodeB
        bond2.nodes[0] = nodeB
        bond2.nodes[1] = nodeC

        // expected result: CH3-CH=CH2
        val agent = ChemAgent()
        agent.addNode(nodeA)
        agent.addBond(bond1)
        agent.addNode(nodeB)
        agent.addBond(bond2)
        agent.addNode(nodeC)

        resolveAutoNodes(agent)

        assertEquals(makeTextFormula(nodeA, rulesText), "CH3")
        assertEquals(makeTextFormula(nodeB, rulesText), "CH")
        assertEquals(makeTextFormula(nodeC, rulesText), "CH2")
        assertEquals(nodeA.bonds, setOf(bond1))
        assertEquals(nodeB.bonds, setOf(bond1, bond2))
        assertEquals(nodeC.bonds, setOf(bond2))
    }
}