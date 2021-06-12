package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.state.stateAgentMid
import charChem.core.ChemBond
import charChem.core.ChemNode

class BranchDecl(pos: Int, val node: ChemNode, val bond: ChemBond?) : StackItem(pos) {
    override fun msgInvalidClose() = "It is necessary to close the branch"
}

// Указатель установлен на символ < или * для случая (*
fun openBranch(compiler: ChemCompiler): Int {
    val curNode: ChemNode = getNodeForBondStart(compiler, null)
    compiler.push(BranchDecl(compiler.pos, curNode, compiler.curBond))
    compiler.chainSys.onBranchBegin()
    compiler.nodesBranch.onBranchBegin()
    return compiler.setState(::stateAgentMid, 1)
}

fun closeBranch(compiler: ChemCompiler): Int {
    val item = compiler.pop()
    item?.let { decl ->
        if (decl !is BranchDecl) {
            // Ошибка: ветка закрывается до того, как закрыта скобка...
            compiler.error("Cant close branch before bracket",
                    listOf("pos" to compiler.pos, "pos0" to decl.pos + 1))
        }
        bindNodeToCurrentBond(compiler, compiler.curNode)
        closeNode(compiler)

        compiler.curNode = decl.node
        compiler.chainSys.onBranchEnd()
        compiler.nodesBranch.onBranchEnd()
        compiler.chainSys.setCurNode(decl.node)

        return compiler.setState(::stateAgentMid, 1)
    } ?: compiler.error("Invalid branch close", listOf("pos" to compiler.pos))
}

fun checkBranch(compiler: ChemCompiler) {
    compiler.pop() ?.let {
        compiler.error(it.msgInvalidClose(), listOf("pos" to it.pos))
    }
}