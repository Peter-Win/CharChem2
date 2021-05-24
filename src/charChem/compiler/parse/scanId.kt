package charChem.compiler.parse

import charChem.compiler.ChemCompiler

fun isIdFirstChar(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z'

fun isIdChar(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z' || c in '0'..'9'

fun isId(text: String): Boolean {
    if (text.isEmpty() || !isIdFirstChar(text[0])) return false
    return text.substring(1).find { !isIdChar(it) } == null
}

fun scanId(compiler: ChemCompiler): String? {
    val startPos = compiler.pos
    if (isIdFirstChar(compiler.curChar())) {
        compiler.pos++
        while (isIdChar(compiler.curChar())) compiler.pos++
    }
    val id = compiler.subStr(startPos)
    return if (id == "") null else id
}