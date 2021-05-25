package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemBond
import charChem.core.ChemNode
import charChem.core.PeriodicTable
import charChem.inspectors.calcMass
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.is0
import charChem.math.roundMass
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

class TestRingBond {
    @Test
    fun testSimpleSquare() {
        val expr = compile("-|`-`|_o")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.size, 4)
    }

    @Test
    fun testSingleRingWithBranches() {
        //    0 CH3
        //      |   3OH
        //  8 / 1 \2/
        //   |  O  |
        //  7 \ 5 /4
        //      |
        //     6Cl
        val expr = compile("CH3|\\</OH>|`/<|Cl>`\\`|/_o")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(
                agent.bonds.map { bond ->
                    bond.nodes.joinToString(bond.tx) { "${it?.index ?: "null"}" }
                },
                listOf("0|1", "1\\2", "2/3", "2|4", "4/5", "5|6", "5\\7", "7|8", "8/1", "1o2o4o5o7o8")
        )
        assertEquals(makeTextFormula(makeBrutto(expr)), "C7H7ClO")
        assertEquals(roundMass(roundMass(calcMass(expr))), roundMass(
                PeriodicTable.C.mass * 7 +
                        PeriodicTable.H.mass * 7 +
                        PeriodicTable.Cl.mass + PeriodicTable.O.mass
        ))
    }

    @Test
    fun testDoubleRing() {
        //  5---1---2
        //  | O | O |
        //  6---4---3
        val expr = compile("-|`-`|_o`-|-_o")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C6H4")
    }

    @Test
    fun testPyrene() {
        val expr = compile("`/|\\/`|`\\_o`|/\\|`/_o|0\\/`|`\\_o`|0/\\|`/_o")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        val bonds = agent.bonds
        fun nodeDef(node: ChemNode?): String = node?.let { "${it.index}" } ?: "null"
        fun bondTx(bond: ChemBond): String =
                if (bond.isAuto)
                    "${bond.dir!!.polarAngleDeg().roundToInt()}${if (is0(bond.n)) "x0" else ""}"
                else bond.linearText()

        fun bondDef(bond: ChemBond) = "${bond.linearText()}: " +
                bond.nodes.joinToString(" ${bondTx(bond)} ") { nodeDef(it) }

        assertEquals(bondDef(bonds[0]), "`/: 0 150 1")
        assertEquals(bondDef(bonds[1]), "|: 1 90 2")
        assertEquals(bondDef(bonds[2]), "\\: 2 30 3")
        assertEquals(bondDef(bonds[3]), "/: 3 -30 4")
        assertEquals(bondDef(bonds[4]), "`|: 4 -90 5")
        assertEquals(bondDef(bonds[5]), "`\\: 5 -150 0")
        assertEquals(bondDef(bonds[6]), "o: 0 o 1 o 2 o 3 o 4 o 5")
        assertEquals(bondDef(bonds[7]), "`|: 0 -90 6")
        assertEquals(bondDef(bonds[8]), "/: 6 -30 7")
        assertEquals(bondDef(bonds[9]), "\\: 7 30 8")
        assertEquals(bondDef(bonds[10]), "|: 8 90 9")
        assertEquals(bondDef(bonds[11]), "`/: 9 150 5")
        assertEquals(bondDef(bonds[12]), "o: 5 o 0 o 6 o 7 o 8 o 9")
        assertEquals(bondDef(bonds[13]), "\\: 4 30 10")
        assertEquals(bondDef(bonds[14]), "/: 10 -30 11")
        assertEquals(bondDef(bonds[15]), "`|: 11 -90 12")
        assertEquals(bondDef(bonds[16]), "`\\: 12 -150 9")
        assertEquals(bondDef(bonds[17]), "o: 9 o 5 o 4 o 10 o 11 o 12")
        assertEquals(bondDef(bonds[18]), "/: 8 -30 13")
        assertEquals(bondDef(bonds[19]), "\\: 13 30 14")
        assertEquals(bondDef(bonds[20]), "|: 14 90 15")
        assertEquals(bondDef(bonds[21]), "`/: 15 150 12")
        assertEquals(bondDef(bonds[22]), "o: 12 o 9 o 8 o 13 o 14 o 15")

        assertEquals(makeTextFormula(makeBrutto(expr)), "C16H10")
    }

    @Test
    fun testRingInBranch() {
        // Закрытие кольца внутри ветки
        // PS. Такое не работает в версиях ниже 1.2
        val expr = compile("|`/`\\`|</\\_o>`\\")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].bonds.map {b -> "${b.linearText()} ${b.nodes.map { it?.index ?: "null" }}" },
        listOf("| [0, 1]", "`/ [1, 2]", "`\\ [2, 3]", "`| [3, 4]", "/ [4, 5]", "\\ [5, 0]",
                "o [0, 1, 2, 3, 4, 5]", "`\\ [4, 6]"))
    }
    @Test
    fun testWithPolygonalBond() {
        //  9 N---CH  4 H2C---CH2 0
        //   /     \     /    |
        // HC 8   5 C--HC 3   |
        //   \     /     \    |
        // 7 HC---CH 6  2 N---CH2 1
        val expr = compile("CH2_(y1.2)CH2_pN_pHC_pH2C_p; #-2`-C`/CH_p6HC_p6HC_p6N_p6CH_p6_o")
        assertEquals(expr.getMessage(""), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.nodes.map { makeTextFormula(it) }, listOf(
                "CH2", "CH2", "N", "HC", "H2C", "C", "CH", "HC", "HC", "N", "CH"
        ))
    }
}