package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.*
import charChem.inspectors.calcMass
import charChem.inspectors.isAbstract
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.lang.Lang
import charChem.math.Point
import charChem.math.roundMass
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private fun cmd2str(cmd: ChemObj): String {
    if (cmd is ChemNode && cmd.autoMode) return "Auto"
    if (cmd is ChemBond && cmd.isNeg) return "`${makeTextFormula(cmd)}"
    return makeTextFormula(cmd)
}

class TestBrackets {
    @Test
    fun testErrorAnotherBracket() {
        val expr = compile("Ca(OH]2")
        assertEquals(expr.getMessage("ru"), "Требуется ) вместо ] в позиции 6")
    }

    @Test
    fun testErrorNotClosed() {
        val expr = compile("Ca(OH2")
        assertEquals(expr.getMessage("ru"), "Необходимо закрыть скобку, открытую в позиции 3")
    }

    @Test
    fun testErrorNotOpen() {
        val expr = compile("CaOH)2")
        assertEquals(expr.getMessage("ru"), "Нет пары для скобки, закрытой в позиции 5")
    }

    @Test
    fun testErrorNestedBranch() {
        val expr = compile("H-C<|(CH2>")
        assertEquals(expr.getMessage("ru"), "Нельзя закрыть ветку в позиции 10, пока не закрыта скобка в позиции 6")
    }

    @Test
    fun testErrorNestedBracket() {
        val expr = compile("H-C<|CH2)>")
        assertEquals(expr.getMessage("ru"), "Нельзя закрыть скобку в позиции 9, пока не закрыта ветка в позиции 4")
    }

    @Test
    fun testSimpleRoundBrackets() {
        val expr = compile("(NH4)2SO4")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "(NH4)2SO4")
        val m = PeriodicTable.H.mass * 8 + PeriodicTable.N.mass * 2 +
                PeriodicTable.O.mass * 4 + PeriodicTable.S.mass
        assertEquals(roundMass(calcMass(expr)), roundMass(m))
        assertEquals(makeTextFormula(makeBrutto(expr)), "H8N2O4S")
    }

    @Test
    fun testSquareBrackets() {
        val expr = compile("K3[Fe(CN)6]")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].commands.map { cmd2str(it) },
                listOf("K3", "[", "Fe", "(", "CN", ")6", "]"))
        assertEquals(makeTextFormula(expr, rulesHtml), "K<sub>3</sub>[Fe(CN)<sub>6</sub>]")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C6FeK3N6")
    }

    @Test
    fun testCharge() {
        val expr = compile("[SO4]^2-")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr, rulesHtml), "[SO<sub>4</sub>]<sup>2-</sup>")
        assertEquals(makeTextFormula(makeBrutto(expr)), "O4S2-")
    }

    @Test
    fun testChargePrefix() {
        val expr = compile("[SO4]`^-2")
        assertEquals(expr.getMessage(), "")
        val bracketEnd = expr.getAgents()[0].commands.last()
        assertTrue(bracketEnd is ChemBracketEnd)
        val charge = bracketEnd.charge
        assertNotNull(charge)
        assertEquals(charge.text, "-2")
        assertTrue(charge.isLeft)
        assertEquals(makeTextFormula(expr, rulesHtml), "<sup>-2</sup>[SO<sub>4</sub>]")
        assertEquals(makeTextFormula(makeBrutto(expr), rulesHtml), "O<sub>4</sub>S<sup>2-</sup>")
    }

    @Test
    fun testInputBond() {
        val expr = compile("H-[Cu<|Br><`|Br>]")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "HBr2Cu")
    }

    @Test
    fun testOutputBond() {
        val expr = compile("[Cu<|Br><`|Br>]-H")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        // 0:[, 1:Cu, 2:|, 3:Br, 4:`|, 5:Br, 6:], 7:-, 8:H
        assertEquals(agent.commands.map { cmd2str(it) },
                listOf("[", "Cu", "|", "Br", "`|", "Br", "]", "-", "H"))
        val bracketEnd = agent.commands[6]
        assertTrue(bracketEnd is ChemBracketEnd)
        val nodeIn = bracketEnd.nodeIn
        assertNotNull(nodeIn)
        assertEquals(makeTextFormula(nodeIn), "Cu")
        val bracketEndBond = bracketEnd.bond
        assertNotNull(bracketEndBond)
        assertEquals(makeTextFormula(bracketEndBond), "-")

        assertEquals(makeTextFormula(makeBrutto(expr)), "HBr2Cu")
    }

    @Test
    fun testInputOutputBond() {
        val expr = compile("H-(CH2)4-Cl")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr, rulesHtml), "H-(CH<sub>2</sub>)<sub>4</sub>-Cl")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H9Cl")
    }

    @Test
    fun testFenestrane5555() {
        // see https://en.wikipedia.org/wiki/Fenestrane
        //  2(H2C)-+-(CH2)2
        //      |  |  |
        //      +--+--+
        //      |  |  |
        //  2(H2C)-+-(CH2)2
        // PS. this formula is not compiled correct by ver 1.0 of CharChem
        val expr = compile("-(CH2)2|`-`|`-(H2C)2|-|<`-(H2C)2`|>-(CH2)2`|")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.size, 12)
        assertFalse(agent.bonds[0].soft)
        assertEquals(agent.commands.map { cmd2str(it) },
                listOf(
                        "Auto", "-", "(", "CH2", ")2", "|", "Auto", "`-", "Auto",
                        "`|", "`-", "(", "H2C", ")2", "|", "Auto", "-",
                        "|", "Auto", "`-", "(", "H2C", ")2", "`|",
                        "-", "(", "CH2", ")2", "`|"
                )
        )
        assertEquals(agent.nodes.size, 9)
        assertEquals(agent.nodes.map { it.pt }, listOf(
                Point(), Point(1.0, 0.0), Point(1.0, 1.0),
                Point(0.0, 1.0), Point(-1.0, 0.0), Point(-1.0, 1.0),
                Point(0.0, 2.0), Point(-1.0, 2.0), Point(1.0, 2.0),
        ))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C13H20")
    }

    @Test
    fun testFenestraneAbs() {
        // see https://en.wikipedia.org/wiki/Fenestrane
        // ┌   ┐     ┌   ┐
        // │ ┌-┼--┬--┼-┐ |     4<---0--->1
        // └ | ┘n |  └ | ┘n    ↓    ↑    ↓
        //   ├----┼----┤       5--->3<---2
        // ┌ | ┐  |  ┌ | ┐     ↑    ↓    ↑
        // | └-┼--┴--┼-┘ |     7<---6--->8
        // └   ┘n    └   ┘n
        // This formula is not correct for ver 1.0 of CharChem
        val expr = compile("-[]'n'|`-`|`-[]'n'|-|<`-[]'n'`|>-[]'n'`|")
        assertEquals(expr.getMessage(), "")
        assertTrue(isAbstract(expr))
        val agent = expr.getAgents()[0]
        val nodes = agent.nodes
        assertEquals(nodes.map { it.pt }, listOf(
                Point(), Point(1.0, 0.0), Point(1.0, 1.0),
                Point(0.0, 1.0), Point(-1.0, 0.0), Point(-1.0, 1.0),
                Point(0.0, 2.0), Point(-1.0, 2.0), Point(1.0, 2.0),
        ))
        assertEquals(nodes.map { makeTextFormula(makeBrutto(it)) }, listOf(
                "CH", "CH2", "CH", "C", "CH2", "CH", "CH", "CH2", "CH2",
        ))
    }

    // Cases from http://charchem.org/ru/new-features

    @Test
    fun testAcetone() {
        val expr = compile("\$ver(1.0)H3C(C=O)CH3")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "H3C(C=O)CH3")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C3H6O")
        assertEquals(calcMass(expr), roundMass(PeriodicTable.C.mass * 3 +
                PeriodicTable.H.mass * 6 + PeriodicTable.O.mass))
    }
    @Test
    fun testPolyChloride() {
        //     ┌       ┐
        //     |/\\    |/\
        //    /|   \\/ |   \
        //  Cl |    |  |    Cl
        //     |    Cl |
        //     └       ┘10
        val expr = compile("\$ver(1.0)Cl/[\\\\<|Cl>]10/\\Cl")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C21H12Cl12")
        assertEquals(expr.getAgents()[0].commands.map { cmd2str(it) }, listOf(
                "Cl", "/", "[", "Auto", "\\\\", "Auto", "|", "Cl", "]10", "/", "Auto", "\\", "Cl"
        ))
        assertEquals(roundMass(calcMass(expr)), roundMass(PeriodicTable.Cl.mass * 2 +
                PeriodicTable.C.mass + PeriodicTable.H.mass * 2 +
                10 * (PeriodicTable.C.mass * 2 + PeriodicTable.H.mass + PeriodicTable.Cl.mass)
        ))
    }
    @Test
    fun testConnectOfLastNodeInBracketsWithNextBond() {
        val expr = compile("[HO/`|O|\\O^-]2_(x1.6,y#-1;-2,N0)Mg^2+")
        assertEquals(expr.getMessage(), "")
    }
}