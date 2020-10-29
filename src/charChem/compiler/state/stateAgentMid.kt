package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parseNode

// Продолжение или конец агента
fun stateAgentMid(compiler: ChemCompiler): Int {
    val res = parseNode(compiler)
    if (res >= 0) return res
    // Конец агента
    return compiler.setState(::stateBegin)
}