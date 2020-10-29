package charChem.core.tests

import charChem.core.ChemK
import org.testng.annotations.Test
import kotlin.test.*

class TestChemK {
    @Test
    fun testText() {
        val k = ChemK("n")
        assertFalse(k.isNumber())
        assertEquals(k.toString(), "n")
        assertTrue(k == ChemK("n"))
        assertTrue(k != ChemK("N"))
        assertTrue(k.equals("n"))
        assertFalse(k.equals("N"))
    }
    @Test
    fun testInt() {
        val k = ChemK(2)
        assertTrue(k.isNumber())
        assertEquals(k.toString(), "2")
        assertTrue(k == ChemK(2.0))
        assertTrue(k != ChemK(1))
        assertTrue(k.equals(2))
        assertTrue(k.equals(2.0))
        assertFalse(k.equals(1))
    }
    @Test
    fun testFloat() {
        val k = ChemK(1.5)
        assertTrue(k.isNumber())
        assertEquals(k.toString(), "1.5")
        assertTrue(k == ChemK(1.5))
        assertTrue(k != ChemK(1.4))
        assertTrue(k.equals(1.5))
        assertFalse(k.equals(1.4))
    }
    @Test
    fun testRound() {
        assertEquals(ChemK(0.96).toString(), "1")
        assertNotEquals(ChemK(0.949).toString(), "1")
        assertEquals(ChemK(1.04).toString(), "1")
        assertNotEquals(ChemK(1.051).toString(), "1")
        assertEquals(ChemK(-0.96).toString(), "-1")
        assertEquals(ChemK(-1.04).toString(), "-1")
    }
}