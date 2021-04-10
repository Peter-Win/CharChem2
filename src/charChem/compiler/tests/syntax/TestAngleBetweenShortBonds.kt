package charChem.compiler.tests.syntax

//import charChem.compiler.ChemCompiler
import charChem.compiler.tests.getBonds
import charChem.compiler.ChemCompiler
//import charChem.core.ChemBond
//import charChem.core.ChemObj
//import charChem.core.Visitor
import charChem.math.Point
import charChem.math.pointFromDeg
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestAngleBetweenShortBonds {
    @Test
    fun testStandard() { // /\
        val expr = ChemCompiler("/\\").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 2)
        assertEquals(bonds[0].dir, pointFromDeg(-30.0))
        assertEquals(bonds[1].dir, pointFromDeg(30.0))
    }
    @Test
    fun testUserSlope() {
        val expr = ChemCompiler("\$slope(45)/\\").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 2)
        assertEquals(bonds[0].dir, pointFromDeg(-45.0))
        assertEquals(bonds[1].dir, pointFromDeg(45.0))
    }
    @Test
    fun testResetUserSlope() {
        val expr = ChemCompiler("\$slope(45)/\\ /\\").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 4)
        assertEquals(bonds[0].dir, pointFromDeg(-45.0))
        assertEquals(bonds[1].dir, pointFromDeg(45.0))
        assertEquals(bonds[2].dir, pointFromDeg(-30.0))
        assertEquals(bonds[3].dir, pointFromDeg(30.0))
    }
    @Test
    fun testNoSoftRightUp() { // H--/
        val expr = ChemCompiler("H--/").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 2)
        assertEquals(bonds[0].dir, Point(1.0, 0.0))
        assertFalse(bonds[0].soft)
        assertEquals(bonds[1].dir, pointFromDeg(-60.0))
    }
    @Test
    fun testSoftRightDown() { // H-\
        val expr = ChemCompiler("H-\\").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 2)
        assertEquals(bonds[0].dir, Point(1.0, 0.0))
        assertTrue(bonds[0].soft)
        assertEquals(bonds[1].dir, pointFromDeg(60.0))
    }

    @Test
    fun testAutoNoSoftRightUp() { // -/
        val expr = ChemCompiler("-/").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 2)
        assertEquals(bonds[0].dir, Point(1.0, 0.0))
        assertFalse(bonds[0].soft)
        assertEquals(bonds[1].dir, pointFromDeg(-60.0))
        assertFalse(bonds[1].soft)
    }
    @Test
    fun testTriangle() {
        val expr = ChemCompiler("-`/").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds[0].dir, Point(1.0, 0.0))
        assertEquals(bonds[1].dir, pointFromDeg(120.0))
    }
    @Test
    fun testCorrectionLeftDownRightDown() {
        val expr = ChemCompiler("`/\\").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds[1].dir, pointFromDeg(60.0))
        assertEquals(bonds[0].dir?.polarAngleDeg(), 0.0)
    }
    @Test
    fun testCorrectionRightDownLeftDown() {
        val expr = ChemCompiler("\\`/").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds[1].dir, pointFromDeg(120.0))
        assertEquals(bonds[0].dir, pointFromDeg(60.0))
    }
    @Test
    fun testCorrectionLeftUpRightUp() {
        val expr = ChemCompiler("`\\/").exec()
        assertEquals(expr.getMessage(), "")
        val bonds = getBonds(expr)
        assertEquals(bonds[1].dir, pointFromDeg(-60.0))
        assertEquals(bonds[0].dir, pointFromDeg(-120.0))
    }
}