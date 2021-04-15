package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.core.ChemK
import charChem.core.ChemNode
import charChem.core.ChemNodeItem
import charChem.core.PeriodicTable
import kotlin.math.roundToInt

fun closeNode(compiler: ChemCompiler) {
    closeItem(compiler)
    compiler.curNode = null
}

fun openNode(compiler: ChemCompiler, isAuto: Boolean = false): ChemNode {
    closeNode(compiler)
    val node = compiler.curAgent!!.addNode(ChemNode())
    node.index = compiler.curAgent!!.nodes.size - 1
    node.autoMode = isAuto
    compiler.curNode = node
    compiler.chainSys.addNode(node)
    bindNodeToCurrentBond(compiler, node)
    compiler.chainSys.addNode(node)
    return node
}

fun getNodeForced(compiler: ChemCompiler, isAuto: Boolean = false): ChemNode {
    return compiler.curNode ?: openNode(compiler, isAuto)
}

// Вызывается в самом конце, когда уже заполнен список bonds
fun updateAutoNode(node: ChemNode) {
    node.items.add(ChemNodeItem(PeriodicTable.C))
    val multipleSum: Double = node.bonds.fold(0.0) { sum, chemBond -> sum + chemBond.n }
    val countH: Int = 4 - multipleSum.roundToInt()
    if (countH > 0) {
        node.items.add(ChemNodeItem(PeriodicTable.H, ChemK(countH)))
    }
}