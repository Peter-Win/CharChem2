package charChem.compiler2.parse

import charChem.compiler2.ChemCompiler

fun scan(compiler: ChemCompiler, isValid: (c: Char) -> Boolean): Boolean {
    while (!compiler.isFinish() && isValid(compiler.curChar())) {
        compiler.pos++
    }
    return !compiler.isFinish()
}

fun scanTo(compiler: ChemCompiler, fin: Char): Boolean = scan(compiler){it != fin}