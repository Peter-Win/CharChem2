package charChem.core.tests

import charChem.core.createCharge
import org.testng.annotations.Test
import kotlin.test.*

class TestChemCharge {
    @Test
    fun testPlus() {
        val one = createCharge("+")
        assertNotNull(one)
        assertEquals(one.text, "+")
        assertEquals(one.value, 1.0)
        assertFalse(one.isRound)
        val two = createCharge("++")
        assertNotNull(two)
        assertEquals(two.text, "++")
        assertEquals(two.value, 2.0)
        assertFalse(two.isRound)
        val three = createCharge("+++")
        assertNotNull(three)
        assertEquals(three.text, "+++")
        assertEquals(three.value, 3.0)
        assertFalse(three.isRound)
        val four = createCharge("++++")
        assertNull(four)
    }
    @Test
    fun testMinus() {
        val one = createCharge("-")
        assertNotNull(one)
        assertEquals(one.text, "-")
        assertEquals(one.value, -1.0)
        assertFalse(one.isRound)
        val two = createCharge("--")
        assertNotNull(two)
        assertEquals(two.text, "--")
        assertEquals(two.value, -2.0)
        assertFalse(two.isRound)
        val three = createCharge("---")
        assertNotNull(three)
        assertEquals(three.text, "---")
        assertEquals(three.value, -3.0)
        assertFalse(three.isRound)
        val four = createCharge("----")
        assertNull(four)
    }
    @Test
    fun testLeftSigned() {
        val p6 = createCharge("+6")
        assertNotNull(p6)
        assertEquals(p6.text, "+6")
        assertEquals(p6.value, 6.0)
        val m2 = createCharge("-2")
        assertNotNull(m2)
        assertEquals(m2.text, "-2")
        assertEquals(m2.value, -2.0)
        val p12 = createCharge("+12")
        assertNotNull(p12)
        assertEquals(p12.text, "+12")
        assertEquals(p12.value, 12.0)
    }
    @Test
    fun testRightSigned() {
        val p6 = createCharge("6+")
        assertNotNull(p6)
        assertEquals(p6.text, "6+")
        assertEquals(p6.value, 6.0)
        val m2 = createCharge("2-")
        assertNotNull(m2)
        assertEquals(m2.text, "2-")
        assertEquals(m2.value, -2.0)
        val p12 = createCharge("12+")
        assertNotNull(p12)
        assertEquals(p12.text, "12+")
        assertEquals(p12.value, 12.0)
        val bad = createCharge("2--")
        assertNull(bad)
    }
    @Test
    fun testRound() {
        val p = createCharge("+o")
        assertNotNull(p)
        assertEquals(p.text, "+")
        assertEquals(p.value, 1.0)
        assertTrue(p.isRound)
        val m = createCharge("-o")
        assertNotNull(m)
        assertEquals(m.text, "-")
        assertEquals(m.value, -1.0)
        assertTrue(m.isRound)
    }

    @Test
    fun testRoman() {
        val one = createCharge("i")
        assertNotNull(one)
        assertEquals(one.value, 1.0)
        assertEquals(one.text, "I")
        val five = createCharge("v")
        assertNotNull(five)
        assertEquals(five.value, 5.0)
        assertEquals(five.text, "V")
        val ten = createCharge("x")
        assertNull(ten)
    }
}