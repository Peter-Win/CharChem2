package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.createChemOp
import charChem.compiler.parse.scanOp
import charChem.compiler.parse.skipSpaces

fun stateBegin(compiler: ChemCompiler): Int {
    skipSpaces(compiler)
    if (compiler.isFinish()) {
        return 0
    }

    if (compiler.curChar() == '"') {
        return compiler.setState(::stateCommentPre, 1)
    }

    val opDef = scanOp(compiler)
    if (opDef != null) {
        createChemOp(compiler, opDef)
        return compiler.setState(::stateOpEnd)
    }

    // Иначе считаем, что это начало реагента
    return compiler.setState(::stateAgent)
}