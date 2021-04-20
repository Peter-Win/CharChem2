package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.createComment

fun stateCommentPre(compiler: ChemCompiler): Int {
    compiler.preComm = createComment(compiler)
    return compiler.setState(::stateBegin)
}