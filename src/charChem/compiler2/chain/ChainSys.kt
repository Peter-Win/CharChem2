package charChem.compiler2.chain

import charChem.compiler2.ChemCompiler
import charChem.core.ChemBond
import charChem.core.ChemNode

var baseChainId = 1
fun generateChainId(): Int = baseChainId++

var baseSubChainId = 1
fun generateSubChainId(): Int = baseSubChainId++

class ChainSys(val compiler: ChemCompiler) {
    var curChainId = generateChainId()
    var curSubChainId = generateSubChainId()

    fun addNode(node: ChemNode) {
        if (node.chain == 0) {
            node.chain = curChainId
            node.subChain = curSubChainId
        }
    }
    fun addBond(bond: ChemBond) {

    }
    fun closeChain() {

    }
}