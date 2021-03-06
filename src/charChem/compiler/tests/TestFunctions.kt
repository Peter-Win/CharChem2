package charChem.compiler.tests

import charChem.compiler.compile
import charChem.currentVersion
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import charChem.math.pointFromDeg
import charChem.textRules.rulesHtml
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestFunctions {
    @Test
    fun testMass() {
        assertEquals(makeTextFormula("\$M(16)O", rulesHtml), "<sup>16</sup>O")
        assertEquals(makeTextFormula("\$nM(235)U", rulesHtml),
                """<span class="echem-mass-and-num">235<br/>92</span>U""")
        assertEquals(makeTextFormula("\$nM(1,0){n}", rulesHtml),
        """<span class="echem-mass-and-num">1<br/>0</span><i>n</i>""")
    }
    @Test
    fun testSlope() {
        val expr = compile("\$slope(20)/\$slope(40)/\$slope(60)/\$slope(0)/")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.size, 4)
        assertEquals(agent.bonds[0].dir, pointFromDeg(-20.0))
        assertEquals(agent.bonds[1].dir, pointFromDeg(-40.0))
        assertEquals(agent.bonds[2].dir, pointFromDeg(-60.0))
        assertEquals(agent.bonds[3].dir, pointFromDeg(-30.0))
    }
    @Test
    fun testL() {
        val expr = compile("|\$L(2)-\$L(1.5)`|\$L()`-")
        assertEquals(expr.getMessage(), "")
        val n = expr.getAgents()[0].nodes
        assertEquals(n.size, 5)
        assertEquals(n[0].pt, Point())
        assertEquals(n[1].pt, Point(0.0, 1.0))
        assertEquals(n[2].pt, Point(2.0, 1.0))
        assertEquals(n[3].pt, Point(2.0, -.5))
        assertEquals(n[4].pt, Point(1.0, -.5))
    }
    @Test
    fun testVer() {
        val hiVer = currentVersion[0]
        val lowVer = currentVersion[1]
        val curVer = "$hiVer.$lowVer"
        assertEquals(compile("\$ver()H2").getMessage(), "")
        assertEquals(compile("\$ver(1)H2").getMessage(), "")
        assertEquals(compile("\$ver(${hiVer+1})H2").getMessage("en"),
                "Formula requires CharChem version ${hiVer+1}.0 instead of $curVer")
        assertEquals(compile("\$ver($hiVer,${lowVer+1})H2").getMessage("en"),
                "Formula requires CharChem version $hiVer.${lowVer+1} instead of $curVer")
        assertEquals(compile("\$ver($hiVer.${lowVer+1}.4)").getMessage("ru"),
        "Для формулы требуется CharChem версии $hiVer.${lowVer+1} вместо $curVer")
    }
}