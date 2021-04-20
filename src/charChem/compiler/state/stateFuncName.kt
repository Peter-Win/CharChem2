package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.funcs.funcsDict
import charChem.compiler.parse.scanArgs
import charChem.compiler.parse.scanTo

fun stateFuncName(compiler: ChemCompiler): Int {
    val startPos = compiler.pos // Указывает на следующий символ за $
    if (!scanTo(compiler, '('))
        compiler.error("Expected '(' after [S]",
                listOf("S" to "$", "pos" to startPos-1))
    val name = compiler.subStr(startPos)
    compiler.pos++
    val (args, argPos) = scanArgs(compiler)
    // Если имя функции не найдено, функция игнорируется
    // с целью совместимости со следующими версиями
    funcsDict[name]?.let { it(compiler, args, argPos) }
    return compiler.setState(::stateAgentMid)
}