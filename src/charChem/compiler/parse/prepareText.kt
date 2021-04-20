package charChem.compiler.parse

import charChem.compiler.ChemCompiler

fun prepareText(compiler: ChemCompiler) {
    compiler.text = compiler.srcText + " "
    compiler.expr.src0 = compiler.text
    compiler.expr.src = compiler.text
}