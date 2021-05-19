package charChem.compiler.main.tests

import charChem.compiler.createTestCompilerWithSingleAgent
import charChem.compiler.main.findNode
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TestFindNode {
    @Test
    fun testFindByIndex() {
        val compiler = createTestCompilerWithSingleAgent("CH3-CH2-OH")
        val agent = compiler.curAgent
        assertNotNull(agent)
        val n1 = findNode(compiler, "1")
        assertNotNull(n1)
        assertEquals(makeTextFormula(n1), "CH3")
        val n3 = findNode(compiler, "3")
        assertNotNull(n3)
        assertEquals(makeTextFormula(n3), "OH")
        assertNull(findNode(compiler, "0"))
        assertNull(findNode(compiler, "4"))
        val neg2 = findNode(compiler, "-2")
        assertNotNull(neg2)
        assertEquals(makeTextFormula(neg2), "CH2")
        val neg1 = findNode(compiler, "-1")
        assertNotNull(neg1)
        assertEquals(makeTextFormula(neg1), "OH")
    }
    @Test
    fun testFindByLabel() {
        val compiler = createTestCompilerWithSingleAgent("Cl-CH2:a-CH2:b-Cl")
        val nodes = compiler.expr.getAgents()[0].nodes
        val nCl = findNode(compiler, "Cl")
        assertNotNull(nCl)
        assertEquals(nodes[0], nCl)
        assertEquals(findNode(compiler, "a"), nodes[1])
        assertEquals(findNode(compiler, "b"), nodes[2])
    }
}