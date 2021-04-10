package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.agentAnalyse

fun stateAgentIn(compiler: ChemCompiler): Int {
    return agentAnalyse(compiler) {
        compiler.error(
                "Unknown element character '[C]'",
                listOf("C" to compiler.curChar(), "pos" to compiler.pos)
        )
    }
}