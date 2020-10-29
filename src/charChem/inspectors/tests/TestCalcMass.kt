package charChem.inspectors.tests

import charChem.compiler.ChemCompiler
import charChem.core.*
import charChem.inspectors.calcMass
import org.testng.annotations.Test
import java.lang.StringBuilder
import kotlin.test.assertEquals

class TestCalcMass {
    @Test
    fun testAtom() {
        val massLi = PeriodicTable.Li.mass
        val expr = ChemCompiler("Li").exec()
        assertEquals(expr.getMessage(), "")
        val agent: ChemAgent = expr.getAgents()[0]
        val node: ChemNode = agent.nodes[0]
        val item: ChemNodeItem = node.items[0]
        assertEquals(calcMass(item), massLi)
        assertEquals(calcMass(node), massLi)
        assertEquals(calcMass(agent), massLi)
        assertEquals(calcMass(expr), massLi)
    }
    @Test
    fun testCoeff() {
        val massH = PeriodicTable.list[0].mass
        val massO = PeriodicTable.O.mass
        val massH2O = massH * 2 + massO
        val expr = ChemCompiler("5H2O ").exec()
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 1)
        val node = agent.nodes[0]
        assertEquals(node.items[0].obj, PeriodicTable.H)
        assertEquals(node.items.size, 2)
        val item1 = node.items[0]
        val item2 = node.items[1]
        assertEquals(calcMass(item2), massO)
        assertEquals(calcMass(item1), massH * 2.0)
        assertEquals(calcMass(node), massH2O)
        assertEquals(calcMass(agent), 5.0 * massH2O)
        assertEquals(calcMass(agent, false), massH2O)
        assertEquals(calcMass(expr), 5.0 * massH2O)
        assertEquals(calcMass(expr, false), massH2O)
        assertEquals(expr.mass()[0], 5.0 * massH2O)
        assertEquals(expr.mass(false)[0], massH2O)
    }
    @Test
    fun testRadical() {
        val expr = ChemCompiler("Me2O").exec()
        assertEquals(expr.getMessage(), "")
        val massH = PeriodicTable.H.mass
        val massC = PeriodicTable.C.mass
        val massO = PeriodicTable.O.mass
        val massMe = massH * 3 + massC
        assertEquals(calcMass(expr), 2 * massMe + massO)
    }
    @Test
    fun testMultiAgent() {
        val expr = ChemCompiler("2H2 + O2 = 2H2O").exec()
        assertEquals(expr.getMessage(), "")
        val agents = expr.getAgents()
        assertEquals(agents[0].n.num, 2.0)
        assertEquals(agents[1].n.num, 1.0)
        assertEquals(agents[2].n.num, 2.0)
        val massH = PeriodicTable.H.mass
        val massO = PeriodicTable.O.mass
        val massH2O = 2 * massH + massO
        val massTotal = calcMass(expr)
        val massList = expr.mass()
        assertEquals(massList, listOf(4 * massH, 2 * massO, 2 * massH2O))
        assertEquals(massTotal, massList.sum())

        assertEquals(calcMass(expr, false), 4.0 * massH + 2.0 * massO + massO)
        assertEquals(expr.mass(false), listOf(2 * massH, 2 * massO, massH2O))
    }
}