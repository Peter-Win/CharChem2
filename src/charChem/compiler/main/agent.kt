package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.scanCoeff
import charChem.compiler.state.stateAgentMid
import charChem.core.*

fun createAgent(compiler: ChemCompiler): ChemAgent {
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
    compiler.varMass = 0.0
    compiler.curWidth = 0
    return agent
}

fun closeChain(compiler: ChemCompiler) {
    compiler.curBond?.let {
        if (it.nodes[1] == null) {
            openNode(compiler, true)
        }
    }
    closeNode(compiler)
    compiler.chainSys.closeChain()
}

fun onCloseAgent(compiler: ChemCompiler) {
    compiler.curAgent?.let {
        checkMul(compiler)
        closeChain(compiler)
        compiler.curAgent = null
        compiler.getAltFlag()
        compiler.curBond = null
        checkBranch(compiler)
        finalUpdateBondsForNodes(it)
        finalUpdateAutoNodes(it)
    }
}

fun star(compiler: ChemCompiler): Int {
    compiler.pos++
    if (compiler.curChar() == ')') {
        return closeBranch(compiler)
    }
    checkMul(compiler)
    startMul(compiler, scanCoeff(compiler) ?: ChemK(1), false)
    return compiler.setState(::stateAgentMid)
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