package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.skipSpaces

fun stateAgentSpace(compiler: ChemCompiler): Int {
    skipSpaces(compiler)
    return compiler.setState(::stateAgentMid)
}