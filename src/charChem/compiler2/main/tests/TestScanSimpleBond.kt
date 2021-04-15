package charChem.compiler2.main.tests

import charChem.compiler2.createTestCompiler
import charChem.compiler2.main.scanSimpleBond
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.test.*

class TestScanSimpleBond {
    @Test
    fun testNotFound() {
        val compiler = createTestCompiler("H2O")
        val bond = scanSimpleBond(compiler)
        assertNull(bond)
        assertEquals(compiler.pos, 0)
    }
    @Test
    fun testSoft1() {
        val compiler = createTestCompiler("Me-OH")
        compiler.pos = 2
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 3)
        assertTrue(bond.soft)
        assertEquals(bond.tx, "-")
        assertEquals(bond.n, 1.0)
        assertEquals(bond.dir, Point(1.0, 0.0))
    }

    @Test
    fun testSoft1Alt() {
        val compiler = createTestCompiler("\u2013Me")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 1)
        assertTrue(bond.soft)
        assertEquals(bond.tx, "-")
        assertEquals(bond.n, 1.0)
    }

    @Test
    fun testSoft2() {
        val compiler = createTestCompiler("=")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 1)
        assertTrue(bond.soft)
        assertEquals(bond.n, 2.0)
        assertEquals(bond.tx, "=")
    }
    @Test
    fun testSoft3() {
        val compiler = createTestCompiler("%")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 1)
        assertEquals(bond.n, 3.0)
        assertTrue(bond.soft)
        assertEquals(bond.tx, "≡")
    }
    @Test
    fun testHardHoriz1() {
        val compiler = createTestCompiler("---")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 2)
        assertFalse(bond.soft)
        assertEquals(bond.tx, "-")
        assertEquals(bond.n, 1.0)
        assertEquals(bond.dir, Point(1.0, 0.0))
    }
    @Test
    fun testHardHoriz3() {
        val compiler = createTestCompiler("%%%")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 2)
        assertFalse(bond.soft)
        assertEquals(bond.tx, "≡")
        assertEquals(bond.n, 3.0)
    }
    @Test
    fun testVert1() {
        val compiler = createTestCompiler("|-`|`-")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 1)
        assertEquals(bond.tx, "|")
        assertEquals(bond.n, 1.0)
        assertEquals(bond.dir, Point(0.0, 1.0))
    }
    @Test
    fun testVert2() {
        val compiler = createTestCompiler("||-`|`-")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 2)
        assertEquals(bond.tx, "||")
        assertEquals(bond.n, 2.0)
    }
    @Test
    fun testVert3() {
        val compiler = createTestCompiler("||||")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 3)
        assertEquals(bond.n, 3.0)
        assertEquals(bond.tx, "|||")
        assertFalse(bond.soft)
    }
    @Test
    fun testSlopeUp3() {
        val compiler = createTestCompiler("////")
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 3)
        assertEquals(bond.n, 3.0)
        assertFalse(bond.soft)
        assertEquals(bond.tx, "///")
        assertEquals(bond.dir, pointFromDeg(-30.0))
    }
    @Test
    fun testAltSlopeUp() {
        val compiler = createTestCompiler("/")
        compiler.setAltFlag() // !
        val bond = scanSimpleBond(compiler)
        assertNotNull(bond)
        assertEquals(compiler.pos, 1)
        assertEquals(bond.n, 1.0)
        assertFalse(bond.soft)
        assertEquals(bond.tx, "/")
        assertEquals(bond.dir, pointFromDeg(150.0))
    }
}