package charChem.compiler2.parse

import charChem.compiler2.ChemCompiler

fun skipSpaces(compiler: ChemCompiler) {
    while (compiler.pos < compiler.text.length && isSpace(compiler.curChar())) compiler.pos++
//    return if (compiler.pos == compiler.text.length) ' ' else compiler.curChar()
}