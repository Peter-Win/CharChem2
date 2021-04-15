package charChem.math

import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PointTest {
    @Test
    fun testToa() {
        assertEquals(toa(1.0), "1")
        assertEquals(toa(1.1), "1.1")
    }
    @Test
    fun testPrimaryConstructor() {
        val p = Point(11.1, 22.2)
        assertEquals(p.x, 11.1)
        assertEquals( p.y, 22.2)
    }

    @Test
    fun testCopyConstructor() {
        val src = Point(11.1, 22.2)
        val dst = Point(src)
        assertEquals(dst.x, 11.1)
        assertEquals(dst.y, 22.2)
    }

    @Test
    fun testToString() {
        val src = Point(11.1, 22.2)
        val s = "$src"
        assertEquals(s, "(11.1, 22.2)")
    }

    @Test
    fun testEquals() {
        val a = Point(1.0, 2.0)
        val b = Point(1.0, 2.0)
        val c = Point()
        val cmpAB = a == b
        val cmpAC = a == c
        assertTrue(cmpAB)
        assertFalse(cmpAC)
    }

    @Test
    fun testPlus() {
        val a = Point(1.0, 2.0)
        var b = Point(10.0, 20.0)
        b += a
        val c = a + Point(-1.0, -1.0)
        assertEquals(b, Point(11.0, 22.0))
        assertEquals(c, Point(0.0, 1.0))
    }

    @Test
    fun testMul() {
        assertEquals(Point(1.0, 2.0) * 3.0, Point(3.0, 6.0))
    }

    @Test
    fun testMinus() {
        var a = Point(11.1, 22.2)
        a -= Point(1.1, 2.2)
        assertEquals(a, Point(10.0, 20.0))
        assertEquals(a - Point(4.0, 5.0), Point(6.0, 15.0))
    }

    @Test
    fun testMini() {
        val a = Point(10.0, 22.0)
        a.mini(Point(11.0, 20.0))
        assertEquals(a, Point(10.0, 20.0))
        a.mini(Point(15.0, 15.0))
        assertEquals(a, Point(10.0, 15.0))
    }

    @Test
    fun testMaxi() {
        val a = Point(10.0, 22.0)
        a.maxi(Point(11.0, 20.0))
        assertEquals(a, Point(11.0, 22.0))
        a.maxi(Point(15.0, 15.0))
        assertEquals(a, Point(15.0, 22.0))
    }
}