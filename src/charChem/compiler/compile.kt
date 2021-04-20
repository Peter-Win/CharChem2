package charChem.compiler

import charChem.compiler.main.closeEntity
import charChem.compiler.parse.prepareText
import charChem.core.ChemError
import charChem.core.ChemExpr

fun compile(text: String): ChemExpr {
    val compiler = ChemCompiler(text)
    try {
        prepareText(compiler)
        while (!compiler.isFinish()) {
            val step = compiler.curState(compiler)
            compiler.pos += step
        }
        closeEntity(compiler)
    } catch (e: ChemError) {
        compiler.expr.error = e
    }
    return compiler.expr
}