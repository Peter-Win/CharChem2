package charChem.compiler.tests.syntax

import org.testng.annotations.Test
import charChem.compiler.ChemCompiler
import charChem.compiler.tests.getBonds
import charChem.compiler.tests.getNodes
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import kotlin.test.assertEquals

class TestCoordinatesInSimpleChain {
    @Test
    fun testVertical() {
        val expr = ChemCompiler("CH3|CH||CH2").exec()
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(expr), "CH3|CH||CH2")
        val bonds = getBonds(expr)
        assertEquals(bonds.size, 2)
        assertEquals(bonds[0].dir, Point(0.0, 1.0))
        assertEquals(bonds[1].dir, Point(0.0, 1.0))
        val nodes = getNodes(expr)
        assertEquals(nodes.size, 3)
        assertEquals(nodes[0].pt, Point())
        assertEquals(bonds[0].calcPt(), Point(0.0, 1.0))
        assertEquals(bonds[0].nodes[1]?.pt, Point(0.0, 1.0))
        assertEquals(nodes[1].pt, Point(0.0, 1.0))
        assertEquals(nodes[2].pt, Point(0.0, 2.0))
    }
}