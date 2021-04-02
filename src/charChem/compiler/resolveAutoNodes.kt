package charChem.compiler

import charChem.core.*
import kotlin.math.roundToInt

// Все узлы получают список связей
// Автоузлы получают объект C и возможно H
fun resolveAutoNodes(agent: ChemAgent) {
    val autoNodes = mutableListOf<ChemNode>()
    agent.walk(object: Visitor() {
        override fun bond(obj: ChemBond) {
            obj.nodes.forEach { node -> node?.let { it.bonds.add(obj) } }
        }
        override fun nodePre(obj: ChemNode) {
            if (obj.autoMode) {
                autoNodes.add(obj)
            }
        }
    })
    autoNodes.forEach {node ->
        node.items.add(ChemNodeItem(PeriodicTable.C))
        val used = node.bonds.fold(0.0) {
            acc, it -> acc + it.n
        }
        val free = 4 - used.roundToInt()
        if (free > 0) {
            val h = ChemNodeItem(PeriodicTable.H)
            h.n = ChemK(free)
            node.items.add(h)
        }
    }
}