package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.parse.createComment

fun stateCommentPre(compiler: ChemCompiler): Int {
    compiler.preComm = createComment(compiler)
    return compiler.setState(::stateBegin)
}