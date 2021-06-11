package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemBond
import charChem.core.ChemExpr
import charChem.core.ChemNode
import charChem.core.ChemObj
import charChem.inspectors.calcCharge
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeElemList
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesCharChem
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

private fun chargeCheck(node: ChemNode): ChemObj = node.charge?.let{
    compile(makeElemList(node).toString())
} ?: node

private fun nodeCvt(node: ChemNode): ChemObj = if (node.autoMode) makeBrutto(node) else chargeCheck(node)

private fun nodeText(node: ChemNode): String = "${node.index}:${makeTextFormula(nodeCvt(node), rulesCharChem)}"

private fun makeNodesText(expr: ChemExpr) = expr.getAgents()[0].nodes.map { nodeText(it) }

private fun bondTextStd(it: ChemBond) = "${it.nodes[0]?.index}" +
        "(${if (it.soft) "~" else ""}${it.dir!!.polarAngleDeg().roundToInt()}" +
        "${if (it.n != 1.0) "*${it.n.roundToInt()}" else ""})" +
        "${it.nodes[1]?.index}"

private fun bondTextExt(bond: ChemBond) = "${bond.linearText()}${bond.nodes.map { it?.index }}"

private fun bondText(bond: ChemBond) = if (bond.nodes.size == 2) bondTextStd(bond) else bondTextExt(bond)

private fun bondInfo(i: Int, it: ChemBond) = "$i:${bondText(it)}"

/**
 * format: <bondIndex>:<srcNodeIndex>([~if soft]<angle>[*<multiplicity> if!=1])<dstNodeIndex>
 */
fun makeBondsInfo(expr: ChemExpr) = expr.getAgents()[0].bonds.mapIndexed { i, it -> bondInfo(i, it) }

private fun diff(actualList: List<String>, needList: List<String>): List<String> =
        actualList.foldIndexed(mutableListOf()) { i, list, s ->
            val need = needList.getOrNull(i)
            if (s != need) {
                list.add("$s≠${
                    if (need != null && need.indexOf(':') >= 0)
                        need.substringAfter(':') else need
                }")
            }
            list
        }

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
        assertEquals(realBonds.foldIndexed(mutableListOf<String>()) { index, acc, s ->
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

    @Test
    fun testBetaCyfluthrin() {
        val expr = compile("\$slope(45)Cl-C<`|Cl>\\\\CH\\|<|CH3><`/H3C>_q3_q3; \$slope()#-1-C<//O>\\O-C<`|H><|C%N>-\\\\-//<-F>`\\`-`/;#-1-/O-\\\\-//`\\`=`/")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        //    Cl 2
        // 0  |
        // Cl-C 1                        29===28
        //     \\                         /   \
        //      CH 3   O 10        23O---24    27
        //       \4   //            /    \\   //
        //       | \8-C 9 H 13 22===     25---26
        //       |5/   \  |12   / 21\
        //    7/ |   11 O-C---- 16    ---F 20
        //  H3C  CH3 6    |    \\   //19
        //             14 C%N  17---18
        //                  15
        val needBonds = listOf(
                "0:0(~0)1", "1:1(-90)2", "2:1(45*2)3", "3:3(45)4", "4:4(90)5",
                "5:5(90)6", "6:5(135)7", "7:5(-30)8", "8:8(-150)4", "9:8(0)9",
                "10:9(-60*2)10", "11:9(60)11", "12:11(~0)12", "13:12(-90)13", "14:12(90)14",
                "15:14(~0*3)15", "16:12(0)16", "17:16(60*2)17", "18:17(0)18", "19:18(-60*2)19",
                "20:19(0)20", "21:19(-120)21", "22:21(180*2)22", "23:22(120)16", "24:21(-60)23",
                "25:23(0)24", "26:24(60*2)25", "27:25(0)26", "28:26(-60*2)27", "29:27(-120)28",
                "30:28(180*2)29", "31:29(120)24"
        )
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        val actualNodes = agent.nodes.map { "${it.index}:${makeTextFormula(nodeCvt(it), rulesCharChem)}" }
        val needNodes = listOf("0:Cl", "1:C", "2:Cl", "3:CH", "4:CH", "5:C", "6:CH3", "7:H3C", "8:CH", "9:C",
                "10:O", "11:O", "12:C", "13:H", "14:C", "15:N", "16:C", "17:CH", "18:CH", "19:C",
                "20:F", "21:C", "22:CH", "23:O", "24:C", "25:CH", "26:CH", "27:CH", "28:CH", "29:CH")
        assertEquals(diff(actualNodes, needNodes), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C22H18Cl2FNO3")
    }

    @Test
    fun testClothianidin() {
        //    O2N 5
        //       \
        //        N 4
        //        ||
        //        C         14 S     Cl 13
        //       /3\         /   \12/
        // H3C--N   N--H2C---9    ||
        //   0  |1  |6   8   \\   N
        //      H 2 H 7       10 / 11
        val expr = compile("H3C-N<|H>/C<`||N`\\O2`N>\\N<|H>-H2C-_(A54,N2)_qN`||</Cl>_qS_q")
        assertEquals(expr.getMessage(), "")
        val needBonds = listOf("0:0(~0)1", "1:1(90)2", "2:1(-60)3", "3:3(-90*2)4", "4:4(-150)5",
                "5:3(60)6", "6:6(90)7", "7:6(~0)8", "8:8(0)9", "9:9(54*2)10",
                "10:10(-18)11", "11:11(-90*2)12", "12:12(-30)13", "13:12(-162)14", "14:14(126)9")
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        val needNodes = listOf("0:H3C", "1:N", "2:H", "3:C", "4:N", "5:O2N", "6:N", "7:H", "8:H2C", "9:C",
                "10:CH", "11:N", "12:C", "13:Cl", "14:S")
        assertEquals(diff(makeNodesText(expr), needNodes), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C6H8ClN5O2S")
    }

    @Test
    fun testCarbofuran() {
        //   5
        // 6/\\0/4    14
        // ||  |  3\ /
        // 7\//1\O / \
        //  8|   2    15
        //   O   O 11
        //  9 \ //
        //  10 |
        //    HN 12
        //      \ 13
        val expr = compile("|_qO_q:a_q_q`\\\\`/||\\/`/|O\\/O`/|HN\\;\$slope(45)`/#a\\")
        assertEquals(expr.getMessage(), "")
        val needBonds = listOf("0:0(90)1", "1:1(18)2", "2:2(-54)3", "3:3(-126)4", "4:4(162)0",
                "5:0(-150*2)5", "6:5(150)6", "7:6(90*2)7", "8:7(30)8", "9:8(-30*2)1",
                "10:8(90)9", "11:9(30)10", "12:10(-30*2)11", "13:10(90)12", "14:12(30)13",
                "15:14(135)3", "16:3(45)15"
        )
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C12H15NO3")
    }
    @Test
    fun testTrilonB() {
        //                   O 14
        //                  ||
        // 2 O   O-0    12 /13\
        //    \\/         |    OH 15
        //     |1    9    N         16
        //    3 \   / \  /11\ 17  [Na+]2
        //      4 N    10    |18
        // 8 HO   |         /\\
        //     \ / 5    20 O- O 19
        //      6
        //      ||
        //      O 7
        val expr = compile("O^-`/`\\O\\|\\N|`/|O`|`\\HO;#N/\\/N<`|/`|O|\\OH_(x1,y1,N0,TSpace)[Na^+]2>\\|\\O`\\`/O^-")
        assertEquals(expr.getMessage(), "")
        val needNodes = listOf(
                "0:O^-", "1:C", "2:O", "3:CH2", "4:N", "5:CH2", "6:C", "7:O", "8:HO", "9:CH2",
                "10:CH2", "11:N", "12:CH2", "13:C", "14:O", "15:OH", "16:Na^+", "17:CH2",
                "18:C", "19:O", "20:O^-"
        )
        assertEquals(diff(makeNodesText(expr), needNodes), listOf())
        val needBonds = listOf(
                "0:0(150)1", "1:1(-150*2)2", "2:1(90)3", "3:3(30)4", "4:4(90)5",
                "5:5(150)6", "6:6(90*2)7", "7:6(-150)8", "8:4(-30)9", "9:9(30)10",
                "10:10(-30)11", "11:11(-90)12", "12:12(-30)13", "13:13(-90*2)14", "14:13(30)15",
                "15:15(45*0)16", "16:11(30)17", "17:17(90)18", "18:18(30*2)19", "19:18(150)20"
        )
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C10H14N2Na2O8")
    }
    @Test
    fun testAnthracene() {
        //   13   6   1
        // 12/ \7/ \0/ \ 2
        //  |   |   |   |
        // 11\ /8\ /5\ / 3
        //   10   9   4
        val expr = compile("/\\|`/`\\`|_o`\\`/|\\/_o;#-2`/`\\`|/\\|0_o")
        assertEquals(expr.getMessage(), "")
        val needBonds = listOf(
                "0:0(-30)1", "1:1(30)2", "2:2(90)3", "3:3(150)4", "4:4(-150)5",
                "5:5(-90)0", "6:o[0, 1, 2, 3, 4, 5]", "7:0(-150)6", "8:6(150)7", "9:7(90)8",
                "10:8(30)9", "11:9(-30)5", "12:o[5, 0, 6, 7, 8, 9]", "13:8(150)10", "14:10(-150)11",
                "15:11(-90)12", "16:12(-30)13", "17:13(30)7", "18:o[8, 10, 11, 12, 13, 7]"
        )
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C14H10")
    }

    @Test
    fun testIsobacteriochlorin() {
        // _(a54,N)
        val expr = compile("|_q:a2_qN_qq<_(a54):a>_q; `-_q<_(a54,N2)#a>_qN_qq<_(a54,N):b>_q; `||_q<_(a54,N2)#b>_qN<`-H>_q<_(a54,N2):c>_q; -_qq<_(a54)#c>_qN<`|H>_q<_(a54)=#a2>_qq")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C20H18N4")
    }
    @Test
    fun testMalonicAcid() {
        //       OH 0
        //      /
        // O=== 1      O 5
        // 2    \    //
        //       3---4
        //            \
        //             OH 6
        // Bond #1 is result of merge of two pseudo-soft bond descriptions
        val expr = compile("OH`/`-O-\\-<//O>\\OH")
        val agent = expr.getAgents()[0]
        val node0 = agent.nodes[0]
        // all nodes in same chain
        assertEquals(agent.nodes.filter { it.chain != node0.chain}. map { it.index }, listOf())
        // all nodes in same sub chain
        assertEquals(agent.nodes.filter { it.subChain != node0.subChain}. map { it.index }, listOf())
        val needNodes = listOf("0:OH", "1:C", "2:O", "3:CH2", "4:C", "5:O", "6:OH")
        assertEquals(diff(makeNodesText(expr), needNodes), listOf())
        val needBonds = listOf("0:0(120)1", "1:1(180*2)2", "2:1(60)3", "3:3(0)4", "4:4(-60*2)5", "5:4(60)6")
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H4O4")
    }
    @Test
    fun testPulegone() {
        // Merge bonds after bracket declaration
        //            O 4  6 7
        //            ||   C(CH3)2
        //          2/3 \5//
        //          |    |
        //      0  /1\  /8
        //      H3C    9
        val expr = compile("H3C/`|/`|O|\\/C<(CH3)2>`/|`/`\\")
        assertEquals(expr.getMessage(), "")
        val needBonds = listOf(
                "0:0(-30)1", "1:1(-90)2", "2:2(-30)3", "3:3(-90*2)4", "4:3(30)5",
                "5:5(-30*2)6", "6:5(90)8", "7:8(150)9", "8:9(-150)1"
        )
        assertEquals(diff(makeBondsInfo(expr), needBonds), listOf())
        val needNodes = listOf("0:H3C", "1:CH", "2:CH2", "3:C", "4:O", "5:C", "6:C", "7:CH3", "8:CH2", "9:CH2")
        assertEquals(diff(makeNodesText(expr), needNodes), listOf())
        assertEquals(makeTextFormula(makeBrutto(expr)), "C10H16O")
    }
    @Test
    fun testHydrogenHexachlororhenateIV() {
        val expr = compile("\$ver(1.0)[Re^4+\$slope(60)\$L(1.4)<--hCl^-></hCl^-><`\\hCl`^-><`-hCl`^-><`/hCl`^-><\\hCl^->]^2-; H^+_(x%w:3,y.5,N0)#Re; H^+_(x%w:3,y-.5,N0)#Re")
        assertEquals(expr.getMessage(), "")
        val needNodes = listOf(
                "0:Re^4+", "1:Cl^-", "2:Cl^-", "3:Cl^-", "4:Cl^-", "5:Cl^-", "6:Cl^-", "7:H^+", "8:H^+")
        assertEquals(diff(makeNodesText(expr), needNodes), listOf())
        assertEquals(calcCharge(expr), 0.0)
        assertEquals(makeTextFormula(makeBrutto(expr)), "H2Cl6Re")
    }
}