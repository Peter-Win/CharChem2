package charChem.core.tests

import charChem.core.findElement
import org.testng.annotations.Test
import kotlin.test.*

class TestChemAtom {
    @Test
    fun testUnstable() {
        assertFalse(findElement("H")!!.isUnstable())
        assertFalse(findElement("D")!!.isUnstable())
        assertFalse(findElement("T")!!.isUnstable())
        assertFalse(findElement("Au")!!.isUnstable())
        assertTrue(findElement("Tc")!!.isUnstable())
        assertTrue(findElement("Pm")!!.isUnstable())
        assertTrue(findElement("Pu")!!.isUnstable())
        assertTrue(findElement("Md")!!.isUnstable())
    }
}