package charChem.math

import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestRect {
    @Test
    fun testConstructorWithNumbers() {
        val rc = Rect(0.0, 1.0, 2.0, 3.0)
        assertEquals(rc.a, Point(0.0, 1.0))
        assertEquals(rc.b, Point(2.0, 3.0))
        assertEquals(rc.left, 0.0)
        assertEquals(rc.top, 1.0)
        assertEquals(rc.right, 2.0)
        assertEquals(rc.bottom, 3.0)
        assertEquals(rc.width, 2.0)
        assertEquals(rc.height, 2.0)
    }
    @Test
    fun testBoundsChange() {
        val rc = Rect()
        assertTrue(rc.isEmpty())
        rc.left = 1.0
        rc.top = 2.0
        rc.right = 11.0
        rc.bottom = 20.0
        assertFalse(rc.isEmpty())
        assertEquals(rc.width, 10.0)
        assertEquals(rc.height, 18.0)
    }
    @Test
    fun testMove() {
        val rc = Rect(Point(-4.0, -3.0), Point(4.0, 3.0))
        rc.move(4.0, 3.0)
        assertEquals(rc, Rect(Point(), Point(8.0, 6.0)))
    }
}