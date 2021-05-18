package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemBond
import charChem.inspectors.makeTextFormula

fun createRingBond(compiler: ChemCompiler) {
    // curChar == 'o'
    compiler.pos++
    val curNode = getNodeForced(compiler, true)
    val nodesFull = compiler.nodesBranch.nodes
    val nodes = nodesFull.subList(0, nodesFull.size-1)
    val j = nodes.lastIndexOf(curNode)
    if (j < 0)
        compiler.error("Cant close ring", listOf("pos" to compiler.pos - 2))
    val bond = ChemBond()
    bond.nodes = nodes.subList(j, nodes.size).toMutableList()
    bond.n = 1.0
    bond.tx = "o"
    compiler.curAgent!!.addBond(bond)
    compiler.curBond = null
}