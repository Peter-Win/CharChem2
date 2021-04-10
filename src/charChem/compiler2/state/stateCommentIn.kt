package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.addNodeItem
import charChem.compiler2.parse.createComment

fun stateCommentIn(compiler: ChemCompiler): Int {
    addNodeItem(compiler, createComment(compiler))
    return compiler.setState(::stateAgentMid)
}