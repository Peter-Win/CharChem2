package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.agentAnalyse

fun stateAgentIn(compiler: ChemCompiler): Int {
    return agentAnalyse(compiler) {
        compiler.error(
                "Unknown element character '[C]'",
                listOf("C" to compiler.curChar(), "pos" to compiler.pos)
        )
    }
}