package charChem.compiler.parse

import charChem.compiler.ChemCompiler

fun skipSpaces(compiler: ChemCompiler) {
    while (compiler.pos < compiler.text.length && isSpace(compiler.curChar())) compiler.pos++
//    return if (compiler.pos == compiler.text.length) ' ' else compiler.curChar()
}