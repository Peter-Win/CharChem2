package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parseOperation

fun stateBegin(compiler: ChemCompiler): Int {
    compiler.closeEntity()
    val c = compiler.skipSpace()
    if (compiler.isFinish())
        return 0
    if (c == '"')
        return compiler.setState(::stateCommentPre, 1)
    val opResult = parseOperation(compiler)
    if (opResult >= 0)
        return opResult
    // Иначе считаем, что это начало реагента
    return compiler.setState(::stateAgent)
}