package charChem.compiler.parse

import charChem.compiler.ChemCompiler
import charChem.compiler.preprocessor.mainPreProcess

fun prepareText(compiler: ChemCompiler) {
    val src0 = compiler.srcText + " "
    val src = mainPreProcess(src0)
    compiler.text = src
    compiler.expr.src0 = src0
    compiler.expr.src = src
}