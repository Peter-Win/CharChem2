package charChem.core.tests

import charChem.core.*
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestChemNodeItem {
    @Test
    fun testWalk() {
        val item = ChemNodeItem(PeriodicTable.list[2], ChemK(2))
        var res = ""
        item.walk(object : Visitor() {
            override fun itemPre(obj: ChemNodeItem) {
                res += "<<"
            }

            override fun itemPost(obj: ChemNodeItem) {
                res += obj.n
                res += ">>"
            }

            override fun atom(obj: ChemAtom) {
                res += obj.id
            }
        })
        assertEquals(res, "<<Li2>>")
    }
}