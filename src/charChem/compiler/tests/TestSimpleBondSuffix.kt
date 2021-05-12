package charChem.compiler.tests

import charChem.compiler.compile
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestSimpleBondSuffix {
    @Test
    fun testZeroBond() {
        //     O--
        //  K+    H+
        val expr = compile("K^+/0O^--\\oH^+")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds[0].n, 0.0)
        assertEquals(bonds[0].style, "")
        assertEquals(bonds[1].n, 0.0)
        assertEquals(bonds[1].style, "")
    }
    @Test
    fun testHydrogenBond() {
        // H
        // 0\   2
        //   O∙∙∙∙∙H
        // 1/       \3  4
        // H         O----H
        val expr = compile("H\\O<`/H>--hH\\O-H")
        assertEquals(expr.getMessage(), "")
        val hBond = expr.getAgents()[0].bonds[2]
        assertEquals(hBond.n, 0.0)
        assertEquals(hBond.style, ":")
    }
    @Test
    fun testBondWidth() {
        val expr = compile("/wO\\ww|`/dO`\\dd`|")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds.map { "${it.w0},${it.w1}" },
                listOf("0,1", "1,0", "0,0", "0,-1", "-1,0", "0,0"))
    }
    @Test
    fun testCrossAndWave() {
        val expr = compile("\$L(1.4)O//xN\\~H")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertTrue(bonds[0].isCross())
        assertEquals(bonds[1].style, "~")
    }
    @Test
    fun testCoord() {
        val expr = compile("\$L(1.4)/vv-vvv\\v")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds.map { "${if (it.arr0) "<" else ""}-${if (it.arr1) ">" else ""}" },
            listOf("<-", "<->", "->"))
    }
}