package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.agentAnalyse
import charChem.compiler.main.closeEntity

fun stateAgentMid(compiler: ChemCompiler): Int {
    return agentAnalyse(compiler) {
        closeEntity(compiler)
        compiler.setState(::stateBegin)
    }
}