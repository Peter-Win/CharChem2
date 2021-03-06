package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemBond
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestReferences {
    @Test
    fun testSpace() {
        val expr = compile("H3C# -CH2#\n -OH")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "H3C-CH2-OH")
    }
    @Test
    fun testRefByDirectIndex() {
        val expr = compile("H-C-H; H|#2|H")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "CH4")
        val agent = expr.getAgents()[0]
        val nodeC = agent.nodes[1]
        assertEquals(makeTextFormula(nodeC), "C")
        assertEquals(nodeC.bonds.size, 4)
    }
    @Test
    fun testRefByReverseIndex() {
        val expr = compile("H-N-H; H|#-3")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "H3N")
        val agent = expr.getAgents()[0]
        val nodeN = agent.nodes[1]
        assertEquals(makeTextFormula(nodeN), "N")
        assertEquals(nodeN.bonds.size, 3)
    }
    @Test
    fun testRefByName() {
        val expr = compile("Cl-C:center-Cl; O||#center")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "CCl2O")
        val agent = expr.getAgents()[0]
        val nodeC = agent.nodes[1]
        assertEquals(makeTextFormula(nodeC), "C")
        assertEquals(nodeC.bonds.size, 3)
    }
    @Test
    fun testRefByFirstAtomName() {
        // 0 1 2  3 4 5  6       7
        // H-C-H; H-C-H; H|#C|#5|H
        val expr = compile("H-C-H; H-C-H; H|#C|#5|H")
        assertEquals(expr.getMessage(), "")
        fun bondDef(bond: ChemBond): String = "${bond.nodes[0]?.index}${bond.linearText()}${bond.nodes[1]?.index}"
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bondDef(bonds[0]), "0-1")
        assertEquals(bondDef(bonds[1]), "1-2")
        assertEquals(bondDef(bonds[2]), "3-4")
        assertEquals(bondDef(bonds[3]), "4-5")
        assertEquals(bondDef(bonds[4]), "6|1")
        assertEquals(bondDef(bonds[5]), "1|4")
        assertEquals(bondDef(bonds[6]), "4|7")
    }
    @Test
    fun testInvalidNumberReference() {
        Lang.curLang = "ru"
        val expr = compile("H-N-H; H|#5")
        assertEquals(expr.getMessage(), "Неправильная ссылка на узел '5' в позиции 11")
    }
    @Test
    fun testZeroReference() {
        Lang.curLang = "ru"
        val expr = compile("H-N-H; H|#0")
        assertEquals(expr.getMessage(), "Неправильная ссылка на узел '0' в позиции 11")
    }
    @Test
    fun testInvalidLabelReference() {
        Lang.curLang = "ru"
        val expr = compile("H-N-H; H|#abc")
        assertEquals(expr.getMessage(), "Неправильная ссылка на узел 'abc' в позиции 11")
    }
    @Test
    fun testInvalidLabel() {
        Lang.curLang = "ru"
        val expr = compile("H-N:2-H; H|#2")
        assertEquals(expr.getMessage(), "Неправильная метка в позиции 5")
    }
}