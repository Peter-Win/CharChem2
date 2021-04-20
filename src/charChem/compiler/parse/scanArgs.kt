package charChem.compiler.parse

import charChem.compiler.ChemCompiler

data class ArgsInfo(val args: List<String>, val argPos: List<Int>)

/**
 * Извлечение списка аргументов.
 * При входе позиция указывает на символ, следующий за (
 * При выходе - на следующий за )
 */
fun scanArgs(compiler: ChemCompiler): ArgsInfo {
    val p0 = compiler.pos
    var prev = p0
    val args = mutableListOf<String>()
    val argPos = mutableListOf<Int>()
    var level = 0
    fun addArg() {
        argPos.add(prev)
        args.add(compiler.subStr(prev))
        prev = compiler.pos + 1
    }
    while (!compiler.isFinish()) {
        val ch = compiler.curChar()
        if (ch == '(') {
            level++
        } else if (ch == ')') {
            if (level == 0) break
            level--
        } else if (ch == ',' && level == 0) {
            addArg()
        }
        compiler.pos++
    }
    if (compiler.isFinish())
        compiler.error("It is necessary to close the bracket", listOf("pos" to p0-1))
    if (p0 != compiler.pos) {
        addArg()
    }
    compiler.pos++
    return ArgsInfo(args, argPos)
}