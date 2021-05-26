package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemNode
import charChem.core.ChemObj
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesCharChem
import org.testng.annotations.Test
import kotlin.test.assertEquals

private fun nodeCvt(node: ChemNode): ChemObj = if (node.autoMode) makeBrutto(node) else node

class TestComplexCases {
    @Test
    fun testPyrrole() {
        val expr = compile("-_pp_pN<_(y.5,Tv)H>_p_pp; \$L(.45)#H|\"1\"; #3\\0\"2\"; #2/0\"3\"; #1`\\0\"4\"; #6`/0\"5\"")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { "${it.linearText()}${if (it.soft) "*" else ""}" },
                listOf("-", "_pp", "_p", "v", "_p", "_pp", "|", "\\0", "/0", "`\\0", "`/0"))
        val node0 = agent.nodes[0]
        assertEquals(node0.bonds.map { it.linearText() }.toSet(), setOf("_pp", "-", "`\\0"))
        assertEquals(agent.nodes.map { makeTextFormula(nodeCvt(it), rulesCharChem) },
                listOf("CH", "CH", "CH", "N", "H", "CH", "\"1\"", "\"2\"", "\"3\"", "\"4\"", "\"5\""))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H5N")
    }
}