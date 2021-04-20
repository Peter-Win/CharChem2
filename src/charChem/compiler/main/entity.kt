package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemObj

fun onCreateEntity(compiler: ChemCompiler, entity: ChemObj) {
    compiler.curEntity = entity
    compiler.expr.entities.add(entity)
}

fun closeEntity(compiler: ChemCompiler) {
    if (compiler.curEntity != null) {
        compiler.curEntity = null
        onCloseAgent(compiler)
        onCloseOp(compiler)
    }
    compiler.preComm = null
}