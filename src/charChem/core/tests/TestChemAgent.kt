package charChem.core.tests

import charChem.core.*
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestChemAgent {
    @Test
    fun testWalk() {
        val n1 = ChemNode()
        n1.items.add(ChemNodeItem(PeriodicTable.list[5]))
        n1.items.add(ChemNodeItem(PeriodicTable.list[0], ChemK(3)))
        val b1 = ChemBond()
        b1.tx = "-"
        val n2 = ChemNode()
        n2.items.add(ChemNodeItem(PeriodicTable.list[7]))
        n2.items.add(ChemNodeItem(PeriodicTable.list[0]))
        val agent = ChemAgent()
        agent.n = ChemK(2)
        agent.addNode(n1)
        agent.addBond(b1)
        agent.addNode(n2)
        var result = ""
        agent.walk(object: Visitor() {
            override fun agentPre(obj: ChemAgent) {
                result += obj.n
                result += "["
            }

            override fun agentPost(obj: ChemAgent) {
                result += "]"
            }

            override fun atom(obj: ChemAtom) {
                result += obj.id
            }

            override fun itemPost(obj: ChemNodeItem) {
                if (obj.n.isSpecified())
                    result += obj.n
            }

            override fun bond(obj: ChemBond) {
                result += obj.tx
            }
        })
        assertEquals(result, "2[CH3-OH]")
    }
}