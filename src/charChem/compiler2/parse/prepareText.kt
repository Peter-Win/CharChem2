package charChem.compiler2.parse

import charChem.compiler2.ChemCompiler

fun prepareText(compiler: ChemCompiler) {
    compiler.text = compiler.srcText + " "
    compiler.expr.src0 = compiler.text
    compiler.expr.src = compiler.text
}