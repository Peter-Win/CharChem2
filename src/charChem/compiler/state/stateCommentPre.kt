package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.convertComment
import charChem.compiler.scanComment
import charChem.core.ChemComment

fun stateCommentPre(compiler: ChemCompiler): Int {
    val pos0 = compiler.pos
    val rawText = scanComment(compiler)
    val dstText = convertComment(rawText)
    val comm = ChemComment(dstText)
    comm.setBounds(pos0 - 1, compiler.pos + 1)
    compiler.commentPre = comm
    return compiler.setState(::stateBegin, 1)
}