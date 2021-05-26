package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemBond
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
    @Test
    fun testAdenosineTriphosphate() {
        //                                 H2N 27
        //                                   |26
        //                          25 N \21/ \\
        //   3 O   7 O   11O        24//  ||   N 28
        //     || 4  || 8  || 12 13   \   ||   |
        // HO--P--O--P--O--P--O--+ 20  N /22\N//29
        //  0  |1    |5    |9    | /O\ |23
        //     OH    OH    OH   14\ _ / 19:a
        //     2     6     10    15| |17
        //                     16 HO OH 18
        val expr = compile("HO-P|OH; O||#-3-O-P|OH;O||#-3-O-P|OH;O||#-3-O-|_(x1,y1,W+,TW+)<|HO>_(x1,T-)<|OH>_(x1,y-1,W-,TW-):a_(x-1.5,y-0.7,T<)O_(x-1.5,y0.7,T<<);||_pN<|#a>_p_ppN_p/<`|H2N>\\\\N|`//N`\\")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        val needBonds = listOf("0-1", "1|2", "3||1", "1-4", "4-5", "5|6", "7||5", "5-8", "8-9", "9|10",
        "11||9", "9-12", "12-13", "13|14", "14W+15", "15|16", "15-17", "17|18", "17W-19", "19<20", "20<<14",
        "21||22", "22_p23", "23|19", "23_p24", "24_pp25", "25_p21", "21/26", "26`|27", "26\\\\28",
        "28|29", "29`//30", "30`\\22")
        fun bondTxt(bond: ChemBond): String = "${bond.nodes[0]?.index}${bond.linearText()}${bond.nodes[1]?.index}"
        val realBonds = agent.bonds.map { bondTxt(it) }
        assertEquals(realBonds.foldIndexed(mutableListOf<String>()){index, acc, s ->
            val need = needBonds.getOrNull(index)
            if (s != need) acc.add("$s≠$need")
            acc
        }, listOf())
        val needNodes = listOf(
                "0:HO", "1:P", "2:OH", "3:O", "4:O", "5:P", "6:OH", "7:O", "8:O", "9:P",
                "10:OH", "11:O", "12:O", "13:CH2", "14:CH", "15:CH", "16:HO", "17:CH", "18:OH", "19:CH",
                "20:O", "21:C", "22:C", "23:N", "24:CH", "25:N", "26:C", "27:H2N", "28:N", "29:CH", "30:N"
        )
        val realNodes = agent.nodes.map { "${it.index}:${makeTextFormula(nodeCvt(it), rulesCharChem)}" }
        assertEquals(realNodes.foldIndexed(mutableListOf<String>()) { index, acc, s ->
            if (needNodes[index] != s) acc.add("$s≠${needNodes[index].substringAfter(':')}")
            acc
        }, listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C10H16N5O13P3")
    }
}