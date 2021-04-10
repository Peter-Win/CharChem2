package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.createAgent

fun stateAgent(compiler: ChemCompiler): Int {
    createAgent(compiler)
    return compiler.setState(::stateAgentIn)
}