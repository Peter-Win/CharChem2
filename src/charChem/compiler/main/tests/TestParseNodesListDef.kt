package charChem.compiler.main.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.createTestCompilerWithSingleAgent
import charChem.compiler.main.parseNodesListDef
import charChem.core.ChemNode
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private fun textNodes(nodes: List<ChemNode?>): List<String> =
        nodes.map { n -> n?.let { makeTextFormula(it) } ?: "NULL" }

class TestParseNodesListDef {
    @Test
    fun testEmptyList() {
        val compiler = createTestCompiler("")
        val res = parseNodesListDef(compiler, "", 0)
        assertNull(res)
    }
    @Test
    fun testSingleChunks() {
        val compiler = createTestCompilerWithSingleAgent("{A}--{B}:b|{C}--{D}")
        val nodes = parseNodesListDef(compiler, "1;b;-1", 0)
        assertNotNull(nodes)
        assertEquals(textNodes(nodes), listOf("A", "B", "D"))
    }
    @Test
    fun testSingleInterval() {
        val compiler = createTestCompilerWithSingleAgent("{A}--{B}:b|{C}--{D}")
        val nodes = parseNodesListDef(compiler, "b:-1", 1)
        assertNotNull(nodes)
        assertEquals(textNodes(nodes), listOf("B", "C", "D"))
    }
    @Test
    fun testIntervals() {
        //       A---B
        //      /     \
        //     H       C
        //     |       |
        //     G       D
        //      \     /
        //       F---E
        val compiler = createTestCompilerWithSingleAgent("\$slope(45){A}--{B}\\{C}|{D}`/{E}`--{F}`\\{G}`|{H}/")
        val nodes = parseNodesListDef(compiler, "1:3;5:7", 1)
        assertNotNull(nodes)
        assertEquals(textNodes(nodes), listOf("A", "B", "C", "E", "F", "G"))
    }
}