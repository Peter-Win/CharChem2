package charChem.compiler.chains

import charChem.core.ChemNode
import charChem.math.Point

class ChainBox {
    private var chainIndexBase: Int = 1
    private var groupsBase: Int = 1
    var curChain: Chain? = null
    val chains = mutableMapOf<Int,Chain>()

    fun clear() {
        chainIndexBase = 1
        groupsBase = 1
        curChain = null
        chains.clear()
    }

    fun findByPoint(pt: Point): ChemNode? = curChain?.findByPoint(pt)

    fun addNode(node: ChemNode) {
        (curChain?:createChain()).addNode(node)
    }
    private fun createChain(): Chain {
        val chain = Chain(chainIndexBase, groupsBase)
        chainIndexBase++
        groupsBase++
        chains[chain.index] = chain
        curChain = chain
        return chain
    }
}