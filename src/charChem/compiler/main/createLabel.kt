package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.scanId
import charChem.compiler.state.stateAgentMid

// Condition of call: compiler.curChar() == ':'
fun createLabel(compiler: ChemCompiler): Int {
    compiler.pos++
    scanId(compiler)?.let {
        compiler.references[it] = getNodeForced(compiler, true)
    } ?: compiler.error("Invalid label", listOf("pos" to compiler.pos))
    return compiler.setState(::stateAgentMid)
}