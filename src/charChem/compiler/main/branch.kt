package charChem.compiler.main

import charChem.compiler.Branch
import charChem.compiler.ChemCompiler
import charChem.compiler.state.stateAgentMid

// Указатель установлен на символ <
fun openBranch(compiler: ChemCompiler): Int {
    if (compiler.curNode == null) {
        openNode(compiler, true)
    }
    compiler.curNode?.let { node ->
        compiler.branchStack.add(0, Branch(compiler.pos, node, compiler.curBond))
    }
    return compiler.setState(::stateAgentMid, 1)
}

fun closeBranch(compiler: ChemCompiler): Int {
    val stack = compiler.branchStack
    if (stack.size == 0) {
        compiler.error("Invalid branch close", listOf("pos" to compiler.pos))
    }
    val branch = compiler.branchStack[0]
    compiler.branchStack.removeAt(0)

    bindNodeToCurrentBond(compiler, compiler.curNode ?: openNode(compiler, true))
    closeNode(compiler)

    compiler.curNode = branch.node
    compiler.chainSys.setCurNode(branch.node)

    return compiler.setState(::stateAgentMid, 1)
}

fun checkBranch(compiler: ChemCompiler) {
    if (compiler.branchStack.size > 0) {
        val pos = compiler.branchStack[0].pos
        compiler.error("It is necessary to close the branch", listOf("pos" to pos))
    }
}