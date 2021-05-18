package charChem.compiler.main

import charChem.core.ChemNode

class NodesBranch {
    private val nbStack = mutableListOf<MutableList<ChemNode>>()
    var nodes = mutableListOf<ChemNode>()

    fun onBranchBegin() {
        val copy = mutableListOf<ChemNode>()
        copy.addAll(nodes)
        nbStack.add(copy)
    }
    fun onBranchEnd() {
        nodes = nbStack.removeAt(0)
    }
    fun onSubChain() {
        nodes.clear()
    }
    fun onNode(node: ChemNode) {
        nodes.add(node)
    }
}