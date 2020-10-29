package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.ChemAgent
import charChem.core.Visitor
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestSomeAgents() {
    @Test
    fun testSpace() {
        val expr = ChemCompiler("H2 O2").exec()
        assertEquals(expr.getMessage(), "")
        val agents: MutableList<ChemAgent> = mutableListOf()
        expr.walk(object : Visitor() {
            override fun agentPre(obj: ChemAgent) {
                agents.add(obj)
            }
        })
        assertEquals(agents.size, 2)
    }
    @Test
    fun testTab() {
        val expr = ChemCompiler("H2\t\tO2").exec()
        assertEquals(expr.getMessage(), "")
        val agents: MutableList<ChemAgent> = mutableListOf()
        expr.walk(object : Visitor() {
            override fun agentPre(obj: ChemAgent) {
                agents.add(obj)
            }
        })
        assertEquals(agents.size, 2)
    }
    @Test
    fun testEol() {
        val expr = ChemCompiler("H2\nO2").exec()
        assertEquals(expr.getMessage(), "")
        val agents: MutableList<ChemAgent> = mutableListOf()
        expr.walk(object : Visitor() {
            override fun agentPre(obj: ChemAgent) {
                agents.add(obj)
            }
        })
        assertEquals(agents.size, 2)
    }
}