package charChem.core.tests

import charChem.core.ChemRadical
import charChem.core.parseElemList
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestChemRadical {
    @Test
    fun testParseElemList() {
        val el = parseElemList("C*4,H*9,O")
        assertEquals(el.list.size, 3)
        assertEquals(el.list[0].key, "C")
        assertEquals(el.list[0].n, 4.0)
        assertEquals(el.list[1].key, "H")
        assertEquals(el.list[1].n, 9.0)
        assertEquals(el.list[2].key, "O")
        assertEquals(el.list[2].n, 1.0)
    }
    @Test
    fun testMe() {
        val me = ChemRadical.dict["Me"]
        assertNotNull(me)
        assertEquals(me.label, "Me")
        assertEquals(me.items.list.size, 2)
        assertEquals(me.items.list[0].key, "C")
        assertEquals(me.items.list[1].key, "H")
    }
    @Test
    fun testNBu() {
        val nBu = ChemRadical.dict["n-Bu"]
        assertNotNull(nBu)
        assertEquals(nBu.label, "n-Bu")
        assertEquals(nBu.items.list.size, 2)
        assertEquals(nBu.items.list[0].key, "C")
        assertEquals(nBu.items.list[1].key, "H")
        assertEquals(nBu.items.list[0].n, 4.0)
        assertEquals(nBu.items.list[1].n, 9.0)
    }
}