package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.compiler2.state.stateCommentIn
import charChem.compiler2.state.stateCustom
import charChem.compiler2.state.stateElement
import charChem.core.ChemAgent

fun createAgent(compiler: ChemCompiler) {
    val preComm = compiler.preComm
    closeEntity(compiler)
    val agent = ChemAgent()
    agent.part = compiler.curPart
    compiler.curAgent = agent
    onCreateEntity(compiler, agent)
    preComm?.let {
        addNodeItem(compiler, preComm)
    }
}

fun onCloseAgent(compiler: ChemCompiler) {
    if (compiler.curAgent != null) {
        closeNode(compiler)
        compiler.curAgent = null
    }
}

fun agentAnalyse(compiler: ChemCompiler, onDefault: () -> Int): Int {
    val c = compiler.curChar()
    if (c in 'A'..'Z') {
        // Извлечь первый заглавный символ элемента. Следующие должны быть маленькими
        compiler.elementStartPos = compiler.pos
        return compiler.setState(::stateElement, 1)
    }
    if (c == '{') {
        return compiler.setState(::stateCustom, 1)
    }
    if (c == '"') {
        return compiler.setState(::stateCommentIn, 1)
    }
    return onDefault()
}