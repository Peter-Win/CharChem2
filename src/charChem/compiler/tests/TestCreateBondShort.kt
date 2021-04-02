package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.createBondShort
import charChem.core.ChemAgent
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.test.*

class TestCreateBondShort {
    @Test
    fun testMinus() {
        val c = ChemCompiler("-H")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.tx, "-")
        assertEquals(bond.slope, 0.0)
        assertTrue(bond.isText)
        assertTrue(bond.soft)
        assertEquals(bond.style, "")
        assertEquals(bond.dir, Point(1.0, 0.0))
        assertEquals(c.pos, 1)
        val node0 = bond.nodes[0]
        assertNotNull(node0)
        assertTrue(node0.autoMode)
        assertNull(bond.nodes[1])
        assertEquals(c.curBond, bond)
        c.closeAgent()
        assertNull(c.curBond)
        val node1 = bond.nodes[1]
        assertNotNull(node1)
        assertTrue(node1.autoMode)
        assertNotEquals(node0, node1)
    }
    @Test
    fun testMinusMinus() {
        val c = ChemCompiler("--H")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.tx, "-")
        assertEquals(bond.slope, 0.0)
        assertTrue(bond.isText)
        assertFalse(bond.soft)
        assertEquals(bond.style, "")
        assertEquals(bond.dir, Point(1.0, 0.0))
        assertEquals(c.pos, 2)
    }
    @Test
    fun testPercent() {
        val c = ChemCompiler("%N")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(bond.n, 3.0)
        assertEquals(bond.tx, "≡")
        assertEquals(bond.slope, 0.0)
        assertTrue(bond.isText)
        assertTrue(bond.soft)
        assertEquals(bond.style, "")
        assertEquals(bond.dir, Point(1.0, 0.0))
        assertEquals(c.pos, 1)
    }
    @Test
    fun test3() {
        val c = ChemCompiler("///N")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 3)
        assertEquals(bond.n, 3.0)
        assertEquals(bond.tx, "///")
        assertEquals(bond.slope, -1.0)
        assertFalse(bond.soft)
        assertEquals(bond.style, "")
        assertEquals(bond.dir, pointFromDeg(-30.0))
        assertFalse(bond.isText)
    }
    @Test
    fun testNeg() {
        val c = ChemCompiler("/N")
        c.prepareText()
        c.createEntity(ChemAgent())
        c.isNegChar = true
        val bond = createBondShort(c)
        assertEquals(bond.dir, pointFromDeg(150.0))
    }
}