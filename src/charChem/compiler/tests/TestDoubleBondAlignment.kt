package charChem.compiler.tests

import charChem.compiler.compile
import charChem.core.ChemExpr
import org.testng.annotations.Test
import kotlin.test.assertEquals

private fun getAligns(expr: ChemExpr): String =
        expr.getAgents()[0].bonds.fold("") { s, b -> s + (b.align ?: '.')}

class TestDoubleBondAlignment {
    @Test
    fun testUniversalWithN() {
        //     /1\\R
        //   0     2
        // L||     |
        //   5     3
        //     \4//M
        val expr = compile("/_(A30,N2r)|_(A150,N2m)`\\_(A-90,N2L)")
        assertEquals(expr.getMessage(), "")
        assertEquals(getAligns(expr), ".r.m.l")
    }
    @Test
    fun testUniversalWithS() {
        val expr = compile("_(A30,S:|r)_(A90,S:|m)_(A30,S:|L)")
        assertEquals(expr.getMessage(), "")
        assertEquals(getAligns(expr), "rml")
    }
    @Test
    fun testFunction() {
        //  ===+   +===+===+   +===+
        //     | R | M   R | L |   ||
        //     +===+       +===+
        val expr = compile("=|\$dblAlign(r)=`|\$dblAlign(M)_(A0,N2,T=)_(A0,N2R,T=)|\$dblAlign(L)_qq4_q4\$dblAlign()=_(a90,N2,T||)")
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 1)
        assertEquals(expr.getAgents()[0].bonds.map { it.linearText() },
                listOf("=", "|", "=", "`|", "=", "=", "|", "_qq4", "_q4", "=", "||"))
        assertEquals(getAligns(expr), "..r.mr.l...")
    }
    @Test
    fun testSuffixes() {
        val expr = compile("=|=r`|=m=x=r|_qq4l_q4=||")
        assertEquals(expr.getMessage(), "")
        assertEquals(getAligns(expr), "..r.m.r.l...")
    }
}