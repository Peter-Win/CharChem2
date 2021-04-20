package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.addNodeItem
import charChem.compiler.parse.createComment

fun stateCommentIn(compiler: ChemCompiler): Int {
    addNodeItem(compiler, createComment(compiler))
    return compiler.setState(::stateAgentMid)
}