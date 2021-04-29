package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.startMul
import charChem.compiler.parse.scanCoeff

fun stateBracketBegin(compiler: ChemCompiler): Int {
    scanCoeff(compiler)?.let { startMul(compiler, it, true) }
    return compiler.setState(::stateAgentMid)
}