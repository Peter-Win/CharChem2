package charChem.compiler.parse

import charChem.compiler.ChemCompiler

fun scanInt(compiler: ChemCompiler): Int? {
    val oldPos = compiler.pos
    if (compiler.curChar() == '-' && compiler.text[oldPos + 1] in '0'..'9') {
        compiler.pos++
    }
    while (compiler.curChar() in '0'..'9') {
        compiler.pos++
    }
    return if (compiler.pos == oldPos) null else compiler.subStr(oldPos).toInt()
}