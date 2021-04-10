package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
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