package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.makeTextFormula
import charChem.textRules.*
import org.testng.annotations.Test
import kotlin.test.assertEquals

private fun span(text: String, color: String) = "<span style=\"color:$color\">$text</span>"
private fun sub(text: String) = "<sub>$text</sub>"
private fun sub(n: Int) = sub(n.toString())
private fun bbColor(text: String, color: String) = "[color=$color]$text[/color]"


class TestMakeTextFormula {
    @Test
    fun testSimple() {
        assertEquals(makeTextFormula("Li", rulesText), "Li")
        assertEquals(makeTextFormula("H2", rulesText), "H2")
        assertEquals(makeTextFormula("H2O", rulesText), "H2O")
        assertEquals(makeTextFormula("2H2O", rulesText), "2H2O")
        assertEquals(makeTextFormula("C'n'H'2n+2'", rulesText), "CnH2n+2")
        assertEquals(makeTextFormula("{R}OH", rulesText), "ROH")
        assertEquals(makeTextFormula("P{Me}3", rulesText), "PMe3")
        assertEquals(makeTextFormula("\"+[Delta]\"CO2\"|^\"", rulesText), "+ΔCO2↑")
        assertEquals(makeTextFormula("2H2 + O2 = 2H2O", rulesText), "2H2 + O2 = 2H2O")
        assertEquals(makeTextFormula("SO4^2-", rulesText), "SO42-")
        assertEquals(makeTextFormula("SO4`^-2", rulesText), "-2SO4")
    }

    @Test
    fun testReverse() {
        assertEquals(makeTextFormula("Br`-(C=O)`-Cl", rulesText), "Cl-(C=O)-Br")
    }

    @Test
    fun testColorSimple() {
        val e1 = compile("\$color(magenta)H2\$color()O")
        assertEquals(e1.getMessage(), "")
        assertEquals(makeTextFormula(e1), "H2O")
        assertEquals(makeTextFormula(e1, rulesHtml), """<span style="color:magenta">H<sub>2</sub></span>O""")
        assertEquals(makeTextFormula(e1, rulesBB), "${bbColor("H[sub]2[/sub]", "magenta")}O")
        assertEquals(makeTextFormula(e1, rulesMhchem), "\\color{magenta}{H_{2}}O")
        assertEquals(makeTextFormula(e1, rulesCharChem), "\$color(magenta)H2\$color()O")
    }

    @Test
    fun testTwoColors() {
        val e2 = compile("\$color(blue)H2\$color(red)S\$color()O4")
        assertEquals(e2.getMessage(), "")
        assertEquals(makeTextFormula(e2), "H2SO4")
        assertEquals(makeTextFormula(e2, rulesHtml),
                span("H"+sub(2), "blue") +
                        span("S", "red") + "O" + sub(4)
        )
        assertEquals(makeTextFormula(e2, rulesBB),
                bbColor("H[sub]2[/sub]", "blue") +
                        bbColor("S", "red") + "O[sub]4[/sub]"
        )
        assertEquals(makeTextFormula(e2, rulesMhchem), "\\color{blue}{H_{2}}\\color{red}{S}O_{4}")
        assertEquals(makeTextFormula(e2, rulesCharChem), "\$color(blue)H2\$color(red)S\$color()O4")
    }

    @Test
    fun testItemColor() {
        val expr = compile("\$color(gray)\$itemColor(brown){R}-OH")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "R-OH")
        assertEquals(makeTextFormula(expr, rulesHtml),
                """<span style="color:brown"><i>R</i></span><span style="color:gray">-</span><span style="color:brown">OH</span>""")
        assertEquals(
                makeTextFormula(expr, rulesBB),
                bbColor("[i]R[/i]", "brown") + bbColor("-", "gray") + bbColor("OH", "brown")
        )
        assertEquals(makeTextFormula(expr, rulesMhchem),
                "\\color{brown}{R}\\color{gray}{-}\\color{brown}{OH}")
        assertEquals(makeTextFormula(expr, rulesCharChem),
                "\$color(brown){R}\$color(gray)-\$color(brown)OH\$color()")
    }

    @Test
    fun testAtomColor() {
        val expr = compile("\$color(black)\$itemColor(blue)\$atomColor(red)Fe2(SO4)3")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "Fe2(SO4)3")
        assertEquals(makeTextFormula(expr, rulesHtml),
                span("Fe", "red") + span(sub(2), "blue") +
                        span("(", "black") + span("SO", "red") + span(sub("4"), "blue") +
                        span(")"+sub(3), "black")
        )
        assertEquals(
                makeTextFormula(expr, rulesBB),
                bbColor("Fe", "red") + bbColor("[sub]2[/sub]", "blue") +
                        bbColor("(", "black") + bbColor("SO", "red") + bbColor("[sub]4[/sub]", "blue") +
                        bbColor(")[sub]3[/sub]", "black")
        )
        assertEquals(makeTextFormula(expr, rulesMhchem),
                "\\color{red}{Fe}\\color{blue}{_{2}}\\color{black}{(}\\color{red}{SO}\\color{blue}{_{4}}\\color{black}{)_{3}}")
    }

    @Test
    fun testOperation() {
        val expr = compile("\$atomColor1(red)3Ba^2+ + 2PO4^3- -> \$atomColor1(red)Ba3(PO4)2\"|v\"")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "3Ba2+ + 2PO43- → Ba3(PO4)2↓")
        assertEquals(
                makeTextFormula(expr, rulesBB),
                "[b]3[/b]${bbColor("Ba", "red")}[sup]2+[/sup] + [b]2[/b]PO[sub]4[/sub][sup]3-[/sup] → ${bbColor("Ba", "red")}[sub]3[/sub](PO[sub]4[/sub])[sub]2[/sub][i]↓[/i]"
        )
        assertEquals(makeTextFormula(expr, rulesMhchem),
                "3\\color{red}{Ba}^{2+} + 2PO_{4}^{3-} -> \\color{red}{Ba}_{3}(PO_{4})_{2}↓")
        assertEquals(makeTextFormula(expr, rulesRTF),
                "3Ba{\\super 2+} + 2PO{\\sub 4}{\\super 3-} {\\cf2\\rtlch \\ltrch\\loch \\u8594\\'3f} Ba{\\sub 3}(PO{\\sub 4}){\\sub 2}{\\cf2\\rtlch \\ltrch\\loch \\u8595\\'3f}")
        // Пока предполагаем, что функции нельзя вставлять между атомом и коэффициентом
//        assertEquals(makeTextFormula(expr, rulesCharChem),
//                "3\$color(red)Ba\$color()^2+ + 2PO4^3- -> \$color(red)Ba\$color()3(PO4)2\"↓\"")
    }

    @Test
    fun testOperationWithComments() {
        val expr = compile("\"[OH-]\"-->\"+[Delta]\"")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "[OH-]—→+Δ")
        assertEquals(makeTextFormula(expr, rulesHtml),
        """<span class="echem-op"><span class="echem-opcomment">[OH-]</span><span class="chem-long-arrow">→</span><span class="echem-opcomment">+Δ</span></span>""")
        assertEquals(makeTextFormula(expr, rulesBB), "[OH-]—→+Δ")
        assertEquals(makeTextFormula(expr, rulesMhchem), "->[{[OH-]}][{+Δ}]")
        assertEquals(makeTextFormula(expr, rulesCharChem), "\"[OH-]\"-->\"+Δ\"")
    }
}