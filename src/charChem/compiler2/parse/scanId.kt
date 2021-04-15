package charChem.compiler2.parse

import charChem.compiler2.ChemCompiler

fun isIdFirstChar(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z'

fun isIdChar(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z' || c in '0'..'9'

fun scanId(compiler: ChemCompiler): String? {
    val startPos = compiler.pos
    if (isIdFirstChar(compiler.curChar())) {
        compiler.pos++
        while (isIdChar(compiler.curChar())) compiler.pos++
    }
    val id = compiler.subStr(startPos)
    return if (id == "") null else id
}