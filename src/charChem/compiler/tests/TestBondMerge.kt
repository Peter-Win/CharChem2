package charChem.compiler.tests

import charChem.compiler.compile
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.test.assertEquals

class TestBondMerge {
    @Test
    fun testKetone() {
        //    0
        //    |1
        //   /\\
        //  3  O 2
        val expr = compile("|\\O`\\`/")
        assertEquals(expr.getMessage(), "")
        val agent = expr.getAgents()[0]
        assertEquals(agent.bonds.map {
            "${it.nodes[0]?.index}(${it.dir!!.polarAngleDeg().roundToInt()}*${it.n.roundToInt()})${it.nodes[1]?.index}"
        }, listOf("0(90*1)1", "1(30*2)2", "1(150*1)3"))
    }
    @Test
    fun testMergeWithRef() {
        //   0---1   bond0 0-1, bond1 1|2, bond2 2`-3 - обычные случаи
        //   | / |   bond3 3`|0 - стыковка с существующим узлом, найденным по вектору связи.
        //   3---2   0-1 - наложение на существующую связь. второй узел найден по вектору.
        //           bond4 1|3 - похоже на наложение, но второй узел указан через ссылку
        //
        val expr = compile("-|`-`|-|#4")
        assertEquals(expr.getMessage(), "")
        val bonds = expr.getAgents()[0].bonds
        assertEquals(bonds[0].nodes[0]?.pt, Point())
        assertEquals(bonds[0].nodes[1]?.pt, Point(1.0, 0.0))
        assertEquals(bonds[0].n, 2.0)

        assertEquals(bonds[3].nodes[1]?.index, 0)

        assertEquals(bonds[4].nodes[0]?.index, 1)
        assertEquals(bonds[4].nodes[1]?.index, 3)
        assertEquals(bonds[4].dir, Point(-1.0, 1.0))
    }
}