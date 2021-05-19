package charChem.compiler

import charChem.compiler.main.closeNode
import charChem.compiler.state.stateAgentMid

fun createTestCompilerWithSingleAgent(text: String): ChemCompiler {
    val compiler = createTestCompiler(text)
    while (!compiler.isFinish()) {
        if (compiler.curChar() == ' ' && compiler.curState == ::stateAgentMid) break
        val step = compiler.curState(compiler)
        compiler.pos += step
    }
    closeNode(compiler)
    return compiler
}