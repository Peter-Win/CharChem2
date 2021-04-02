package charChem.compiler.chains

import charChem.core.ChemNode
import charChem.math.Point

class Chain(val index: Int, var group: Int) {
    val nodes = mutableListOf<ChemNode>()

    fun findByPoint(pt: Point): ChemNode? = nodes.find { it.pt == pt }

    fun addNode(node: ChemNode) {
        nodes.add(node)
        node.chain = index
    }
}