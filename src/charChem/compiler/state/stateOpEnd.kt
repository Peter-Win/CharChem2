package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.createComment

fun stateOpEnd(compiler: ChemCompiler): Int {
    if (compiler.curChar() == '"') {
        compiler.pos++
        compiler.curOp!!.commentPost = createComment(compiler)
    }
    return compiler.setState(::stateBegin)
}