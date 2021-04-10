package charChem.compiler2

import charChem.compiler2.main.closeEntity
import charChem.compiler2.parse.prepareText
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