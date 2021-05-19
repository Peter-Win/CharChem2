package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemBond
import charChem.core.ChemNode
import charChem.inspectors.makeTextFormula

fun findRingNodes(compiler: ChemCompiler): MutableList<ChemNode?>? {
    val curNode = getNodeForced(compiler, true)
    val nodesFull = compiler.nodesBranch.nodes
    val nodes = nodesFull.subList(0, nodesFull.size-1)
    val j = nodes.lastIndexOf(curNode)
    return if (j < 0) {
        null
    } else {
        nodes.subList(j, nodes.size).toMutableList()
    }
}

fun findRingNodesEx(compiler: ChemCompiler): MutableList<ChemNode?> =
    findRingNodes(compiler) ?:
    compiler.error("Cant close ring", listOf("pos" to compiler.pos - 2))


fun createRingBond(compiler: ChemCompiler, deltaPos: Int) {
    // curChar == 'o'
    compiler.pos += deltaPos
    val bond = createCommonBond(compiler)
    bond.nodes = findRingNodesEx(compiler)
    bond.n = 1.0
    bond.tx = "o"
    bond.isCycle = true
    compiler.curAgent!!.addBond(bond)
    compiler.curBond = null
}