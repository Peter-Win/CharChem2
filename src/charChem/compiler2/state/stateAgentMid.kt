package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.agentAnalyse
import charChem.compiler2.main.closeEntity

fun stateAgentMid(compiler: ChemCompiler): Int {
    return agentAnalyse(compiler) {
        closeEntity(compiler)
        compiler.setState(::stateBegin)
    }
}