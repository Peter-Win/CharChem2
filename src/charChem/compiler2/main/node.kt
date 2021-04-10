package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.core.ChemNode

fun closeNode(compiler: ChemCompiler) {
    closeItem(compiler)
    compiler.curNode = null
}

fun openNode(compiler: ChemCompiler): ChemNode {
    closeNode(compiler)
    val node = compiler.curAgent!!.addNode(ChemNode())
    compiler.curNode = node
    compiler.chainSys.addNode(node)
    return node
}

fun getNodeForced(compiler: ChemCompiler): ChemNode {
    return compiler.curNode ?: openNode(compiler)
}