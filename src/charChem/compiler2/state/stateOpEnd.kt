package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.parse.createComment

fun stateOpEnd(compiler: ChemCompiler): Int {
    if (compiler.curChar() == '"') {
        compiler.pos++
        compiler.curOp!!.commentPost = createComment(compiler)
    }
    return compiler.setState(::stateBegin)
}