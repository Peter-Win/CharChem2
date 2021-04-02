package charChem.core.tests

import charChem.core.ChemBond
import charChem.core.ChemNode
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestChemBond {
    @Test
    fun testCalcPt() {
        val bond = ChemBond()
        bond.nodes[0] = ChemNode(Point(1.0, 2.0))
        bond.dir = Point(2.0, 1.0)
        val p2: Point = bond.calcPt()
        assertEquals(p2, Point(3.0, 3.0))
    }

    @Test
    fun testOther() {
        val nodeA = ChemNode(Point(0.0, 0.0))
        val nodeB = ChemNode(Point(1.0, 1.0))
        val nodeC = ChemNode(Point(2.0, 2.0))
        val bond = ChemBond()
        bond.nodes = arrayOf(nodeA, nodeB)
        assertEquals(bond.other(nodeA), nodeB)
        assertEquals(bond.other(nodeB), nodeA)
        assertNull(bond.other(nodeC))
    }
}