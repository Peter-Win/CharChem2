package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestUniversalBondProps {
    @Test
    fun testMultiplicity() {
        val expr = compile("_(A-60,N2m)_(A0,N2x)_(A60,N0)\"(R)\"")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].bonds.map { "${it.n}${it.align?:""}${it.style}" },
            listOf("2.0m", "2.0x", "0.0"))
    }
    @Test
    fun testStyles() {
        val expr = compile("_(A0,N1.5,S:|r)_(A90,N1.5,S:|r)_(A180,N1.5,S:|r)_(A-90,N1.5,S:|r)")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].bonds.map { "${it.n}${it.align?:""}${it.style}" },
                listOf("1.5r:|", "1.5r:|", "1.5r:|", "1.5r:|"))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H4")
    }
    @Test
    fun testWidth() {
        //  0        w-3==4        D+7||8        D-11--
        //   \      /      \      /      \      /
        //    W+1==2        W-5--6        d+9||10
        val expr = compile("_(A60,W+)_(A0)_(A-60,w-)_(A0)_(A60,W-)_(A0)_(A-60,D+)_(A0)_(A60,d+)_(A0)_(A-60,D-)_(A0)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { "${it.w0},${it.w1}" }, listOf(
                "0,1", "1,1", "1,0", "1,1", "1,0", "0,0", "0,-1", "-1,-1", "0,-1", "-1,-1", "-1,0", "0,0"
        ))
    }
    @Test
    fun testCoordBond() {
        val expr = compile("_(A60,C-)_(A0,C+)_(A-60,C)_(A0)_(A60,<)_(A0,>)")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map { "${if (it.arr0) "<" else ""}-${if (it.arr1) ">" else ""}" },
            listOf("<-", "<->", "->", "-", "<-", "->"))
    }
    @Test
    fun testHydrogenBond() {
        val expr = compile("O_(A30,L2,H)H_(A-30,L2,~)N")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.getAgents()[0].bonds.map { it.style }, listOf(":", "~"))
    }
    @Test
    fun testCustomSoftBond() {
        val expr = compile("CH3_(A10,h)CH2")
        assertEquals(expr.getMessage(), "")
        val bond = expr.getAgents()[0].bonds[0]
        assertTrue(bond.soft)
        assertEquals(bond.dir!!.polarAngleDeg().roundToInt(), 10)
        assertEquals((bond.dir!!.length() * 100).roundToInt(), 100)
    }
    @Test
    fun testCustomText() {
        val expr = compile("H3N_(x2,>,T-->)Pt")
        assertEquals(expr.getMessage(), "")
        val bond = expr.getAgents()[0].bonds[0]
        assertEquals(bond.tx, "-->")
    }
}