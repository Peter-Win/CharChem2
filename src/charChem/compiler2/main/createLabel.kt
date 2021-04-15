package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.compiler2.parse.scanId
import charChem.compiler2.state.stateAgentMid

// Condition of call: compiler.curChar() == ':'
fun createLabel(compiler: ChemCompiler): Int {
    compiler.pos++
    scanId(compiler)?.let {
        compiler.references[it] = getNodeForced(compiler, true)
    } ?: compiler.error("Invalid label", listOf("pos" to compiler.pos))
    return compiler.setState(::stateAgentMid)
}