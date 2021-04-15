package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.compiler2.state.*
import charChem.core.ChemAgent
import charChem.core.ChemBond
import charChem.core.ChemNode
import charChem.core.Visitor

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
    compiler.references.clear()
}

fun closeChain(compiler: ChemCompiler) {
    compiler.curBond?.let {
        if (it.nodes[1] == null) {
            openNode(compiler, true)
        }
    }
    closeNode(compiler)
}

fun onCloseAgent(compiler: ChemCompiler) {
    compiler.curAgent?.let {
        closeChain(compiler)
        compiler.curAgent = null
        compiler.getAltFlag()
        compiler.curBond = null
        finalUpdateBondsForNodes(it)
        finalUpdateAutoNodes(it)
    }
}

fun agentAnalyse(compiler: ChemCompiler, onDefault: () -> Int): Int {
    val c = compiler.curChar()
    val bond = scanSimpleBond(compiler)
    if (bond != null) {
        createSimpleBond(compiler, bond)
        return compiler.setState(::stateAgentMid)
    }
    return when (c) {
        in 'A'..'Z' -> {
            // Извлечь первый заглавный символ элемента. Следующие должны быть маленькими
            compiler.elementStartPos = compiler.pos
            compiler.setState(::stateElement, 1)
        }
        '`' -> {
            compiler.setAltFlag()
            compiler.setState(::stateAgentMid, 1)
        }
        '{' ->
            compiler.setState(::stateCustom, 1)
        '"' ->
            compiler.setState(::stateCommentIn, 1)
        ';' -> {
            closeChain(compiler)
            compiler.setState(::stateAgentSpace, 1)
        }
        ':' ->
            createLabel(compiler)
        '#' ->
            compiler.setState(::stateNodeRef, 1)
        else -> onDefault()
    }
}

fun finalUpdateBondsForNodes(agent: ChemAgent) {
    // update bonds field for all nodes
    agent.walk(object: Visitor() {
        override fun bond(obj: ChemBond) {
            obj.nodes.forEach { it?.bonds?.add(obj) }
        }
    })
}
fun finalUpdateAutoNodes(agent: ChemAgent) {
    agent.walk(object: Visitor() {
        override fun nodePre(obj: ChemNode) {
            if (obj.autoMode) {
                updateAutoNode(obj)
            }
        }
    })
}