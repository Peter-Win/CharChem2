package charChem.compiler.parse

import charChem.compiler.ChemCompiler

fun scan(compiler: ChemCompiler, isValid: (c: Char) -> Boolean): Boolean {
    while (!compiler.isFinish() && isValid(compiler.curChar())) {
        compiler.pos++
    }
    return !compiler.isFinish()
}

fun scanTo(compiler: ChemCompiler, fin: Char): Boolean = scan(compiler){it != fin}