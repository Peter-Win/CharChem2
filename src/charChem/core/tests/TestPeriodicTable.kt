package charChem.core.tests

import charChem.core.findElement
import org.testng.annotations.Test
import kotlin.test.*

class TestPeriodicTable {
    @Test
    fun testFindElement() {
        val br = findElement("Br")
        assertNotNull(br)
        assertEquals(br.id, "Br")
        assertEquals(br.n, 35)
        assertEquals(br.mass, 79.904)
        assertFalse(br.isUnstable())
    }

    @Test
    fun testFindIsotope() {
        val d = findElement("D")
        assertNotNull(d)
        assertEquals(d.id, "D")
        assertEquals(d.n, 1)
        assertFalse(d.isUnstable())
    }

    @Test
    fun testUnstable() {
        val rn = findElement("Rn")
        assertNotNull(rn)
        assertEquals(rn.id, "Rn")
        assertEquals(rn.mass, 222.0)
        assertTrue(rn.isUnstable())
    }

    @Test
    fun testNotFound() {
        val wrong = findElement("Me")
        assertNull(wrong)
    }
}