package charChem.core.tests

import charChem.core.*
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestChemNode {
    @Test
    fun testWalk() {
        val node = ChemNode()
        node.items.add(ChemNodeItem(PeriodicTable.list[5], ChemK(2)))
        node.items.add(ChemNodeItem(PeriodicTable.list[0], ChemK(5)))
        node.items.add(ChemNodeItem(PeriodicTable.list[7]))
        node.items.add(ChemNodeItem(PeriodicTable.list[0]))
        var res = ""
        node.walk(object : Visitor() {
            override fun nodePre(obj: ChemNode) {
                res += "["
            }

            override fun nodePost(obj: ChemNode) {
                res += "]"
            }

            override fun atom(obj: ChemAtom) {
                res += obj.id
            }

            override fun itemPost(obj: ChemNodeItem) {
                if (obj.n.isSpecified()) {
                    res += obj.n
                }
            }
        })
        assertEquals(res, "[C2H5OH]")
    }
    @Test
    fun testIsEmptyNode() {
        fun makeNode(subObj: ChemSubObj): ChemNode {
            val node = ChemNode()
            node.items.add(ChemNodeItem(subObj))
            return node
        }
        assertFalse(isEmptyNode(makeNode(PeriodicTable.list[0])))
        assertFalse(isEmptyNode(makeNode(ChemCustom("R"))))
        assertTrue(isEmptyNode(makeNode(ChemCustom(""))))
        assertFalse(isEmptyNode(makeNode(ChemComment("Text"))))
        assertTrue(isEmptyNode(makeNode(ChemComment(""))))
    }
}