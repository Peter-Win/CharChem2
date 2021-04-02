package charChem.compiler.tests

import charChem.compiler.findBondDef
import charChem.compiler.isSubEqual
import org.testng.annotations.Test
import kotlin.test.*

class TestBondDef {
    @Test
    fun testIsSubEqual() {
        assertTrue(isSubEqual("Go", "Good", 0))
        assertFalse(isSubEqual("Good", "Go", 0))
        assertTrue(isSubEqual("AA", "AAA", 0))
        assertTrue(isSubEqual("AA", "AAA", 1))
        assertFalse(isSubEqual("AA", "AAA", 2))
    }
    @Test
    fun testFindMinus() {
        val a = findBondDef("---", 2)
        assertNotNull(a)
        assertEquals(a.descr, "-")
        val b = findBondDef("---", 1)
        assertNotNull(b)
        assertEquals(b.descr, "--")
        val c = findBondDef("---", 0)
        assertNotNull(c)
        assertEquals(c.descr, "--")
    }
    @Test
    fun testFindSlash() {
        val a = findBondDef("////", 3)
        assertNotNull(a)
        assertEquals(a.descr, "/")
        val b = findBondDef("////", 2)
        assertNotNull(b)
        assertEquals(b.descr, "//")
        val c = findBondDef("////", 1)
        assertNotNull(c)
        assertEquals(c.descr, "///")
        val d = findBondDef("////", 0)
        assertNotNull(d)
        assertEquals(d.descr, "///")
        assertNull(findBondDef(" ////", 0))
    }
}