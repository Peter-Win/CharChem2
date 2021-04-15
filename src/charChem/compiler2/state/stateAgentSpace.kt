package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.parse.skipSpaces

fun stateAgentSpace(compiler: ChemCompiler): Int {
    skipSpaces(compiler)
    return compiler.setState(::stateAgentMid)
}