package charChem.core.tests

import charChem.core.*
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TestElemList {
    @Test
    fun testCreateElemRecById() {
        val elemRec = ElemRec("Li")
        assertEquals(elemRec.id, "Li")
        assertNotNull(elemRec.elem)
        assertEquals(elemRec.elem.n, 3)
        assertEquals(elemRec.elem.id, "Li")
        assertEquals(elemRec.n, 1.0)
        assertEquals(elemRec.key, "Li")
    }

    @Test
    fun testAddElem() {
        val elemList = ElemList()
        elemList.addElem("H", 3.0)
        assertEquals(elemList.list.size, 1)
        assertEquals(elemList.list[0].id, "H")
        assertEquals(elemList.list[0].elem, PeriodicTable.list[0])
        assertEquals(elemList.list[0].n, 3.0)
    }

    @Test
    fun testAddCustom() {
        val elemList = ElemList()
        elemList.addCustom("Pr")
        assertEquals(elemList.list.size, 1)
        assertEquals(elemList.list[0].id, "Pr")
        assertEquals(elemList.list[0].key, "{Pr}")
        assertEquals(elemList.list[0].n, 1.0)
        assertNull(elemList.list[0].elem)
        assertEquals(elemList.charge, 0.0)
    }

    @Test
    fun testAddList() {
        val list1 = ElemList(mutableListOf(ElemRec("C"), ElemRec("H", 3.0)))
        val list2 = ElemList(mutableListOf(ElemRec("C"), ElemRec("O")), -1.0)
        list1.addList(list2)
        assertEquals(list1.list.size, 3)
        assertEquals(list1.charge, -1.0)
        assertEquals(list1.list[0].id, "C")
        assertEquals(list1.list[0].n, 2.0)
    }

    @Test
    fun testAddRadical() {
        val el = ElemList()
        el.addRadical(ChemRadical.dict["Ph"]!!)
        assertEquals(el.charge, 0.0)
        assertEquals(el.list.size, 2)
        assertEquals(el.list[0].key, "C")
        assertEquals(el.list[0].n, 6.0)
    }

    @Test
    fun testToString() {
        val el = ElemList()
        el.addRadical(ChemRadical.dict["Ph"]!!)
        assertEquals(el.toString(), "C6H5")
        el.charge = 1.0
        assertEquals(el.toString(), "C6H5^+")
        el.charge = -1.0
        assertEquals(el.toString(), "C6H5^-")
        el.charge = 2.0
        assertEquals(el.toString(), "C6H5^2+")
        el.charge = -2.0
        assertEquals(el.toString(), "C6H5^2-")
    }

    @Test
    fun testScale() {
        val el = ElemList()
        el.addElem("N")
        el.addElem("O", 3.0)
        el.charge = -1.0
        assertEquals(el.toString(), "NO3^-")
        el.scale(1.0)
        assertEquals(el.toString(), "NO3^-")
        el.scale(2.0)
        assertEquals(el.toString(), "N2O6^2-")
    }

    @Test
    fun testSortByHill() {
        val b12 = parseElemList("P,O*14,N*14,Co,H*88,C*63")
        assertEquals(b12.toString(), "PO14N14CoH88C63")
        b12.sortByHill()
        assertEquals(b12.toString(), "C63H88CoN14O14P")
        val sulfonylurea = ElemList().addCustom("R2")
                .addElem("C", 6.0).addElem("H", 4.0)
                .addElem("S").addElem("O", 2.0)
                .addElem("N").addElem("H")
                .addElem("C").addElem("O")
                .addElem("N").addElem("H")
                .addCustom("R1")
        assertEquals(sulfonylurea.toString(), "{R2}C7H6SO3N2{R1}")
        sulfonylurea.sortByHill()
        assertEquals(sulfonylurea.toString(), "C7H6N2O3S{R1}{R2}")
    }
}