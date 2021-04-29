package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.*
import charChem.inspectors.calcMass
import charChem.inspectors.isAbstract
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.roundMass
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun cmd2str(cmd: ChemObj): String = when (cmd) {
    is ChemMul -> "${if (cmd.isFirst) "" else "*"}${cmd.n}{{"
    is ChemMulEnd -> "}}"
    else -> if (cmd is ChemNode && cmd.autoMode) "Auto" else makeTextFormula(cmd)
}

private fun getCommands(expr: ChemExpr): List<String> =
        expr.getAgents()[0].commands.map { cmd2str(it) }

class TestMul {
    @Test
    fun testFirstCoeff() {
        val expr = compile("[2H2O]")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCommands(expr), listOf("[", "2{{", "H2O", "}}", "]"))
        assertEquals(makeTextFormula(expr, rulesHtml), "[2H<sub>2</sub>O]")
    }

    @Test
    fun testSimple() {
        val expr = compile("CuSO4*5H2O")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCommands(expr), listOf("CuSO4", "*5{{", "H2O", "}}"))
        assertEquals(makeTextFormula(expr), "CuSO4∙5H2O")
        assertEquals(makeTextFormula(expr, rulesHtml), "CuSO<sub>4</sub>∙5H<sub>2</sub>O")
        val mH = PeriodicTable.H.mass
        val mO = PeriodicTable.O.mass
        val mS = PeriodicTable.S.mass
        val mCu = PeriodicTable.Cu.mass
        assertEquals(roundMass(calcMass(expr)), roundMass(mCu + mS + mO * 4 + 5 * (mH * 2 + mO)))
    }

    @Test
    fun testNestedBrackets() {
        val expr = compile("[2Ca(OH)2*3Mg(OH)2]")
        assertEquals(expr.getMessage(), "")
        assertEquals(getCommands(expr), listOf("[", "2{{", "Ca", "(", "OH", ")2", "}}",
                "*3{{", "Mg", "(", "OH", ")2", "}}", "]"))
        val mOH2 = 2 * (PeriodicTable.H.mass + PeriodicTable.O.mass)
        val mCa = PeriodicTable.Ca.mass
        val mMg = PeriodicTable.Mg.mass
        assertEquals(roundMass(calcMass(expr)), roundMass(2 * (mCa + mOH2) + 3 * (mMg + mOH2)))
        assertEquals(makeTextFormula(makeBrutto(expr)), "H10Ca2Mg3O10")
    }
    @Test
    fun testAbstract() {
        // Limonite
        val expr = compile("FeO(OH)*'n'H2O")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "FeO(OH)∙nH2O")
        assertTrue(isAbstract(expr))
    }
}