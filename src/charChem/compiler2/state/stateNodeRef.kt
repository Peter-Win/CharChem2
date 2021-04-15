package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.bindNodeToBond
import charChem.compiler2.main.bindNodeToCurrentBond
import charChem.compiler2.parse.isSpace
import charChem.compiler2.parse.scanId
import charChem.compiler2.parse.scanInt
import charChem.core.ChemNode

fun onReferenceError(compiler: ChemCompiler, ref: String, pos: Int): Nothing {
    compiler.error("Invalid node reference '[ref]'", listOf("ref" to ref, "pos" to pos))
}

fun useRef(compiler: ChemCompiler, node: ChemNode) {
    bindNodeToCurrentBond(compiler, node)
}

fun useRefByNumber(compiler: ChemCompiler, n: Int, startPos: Int) {
    val nodes: List<ChemNode> = compiler.curAgent!!.nodes
    val index: Int = if (n < 0) nodes.size + n else n - 1
    if (index < 0 || index >= nodes.size) {
        onReferenceError(compiler, n.toString(), startPos)
    }
    useRef(compiler, nodes[index])
}

fun useRefById(compiler: ChemCompiler, id: String, startPos: Int) {
    compiler.references[id]?.let {
        useRef(compiler, it)
    } ?: onReferenceError(compiler, id, startPos)
}

fun stateNodeRef(compiler: ChemCompiler): Int {
    if (isSpace(compiler.curChar())) {
        return compiler.setState(::stateAgentSpace)
    }
    val startPos = compiler.pos
    val n = scanInt(compiler)
    if (n != null) {
        useRefByNumber(compiler, n, startPos)
    } else {
        val id = scanId(compiler)
        if (id != null) {
            useRefById(compiler, id, startPos)
        } else {
            onReferenceError(compiler, compiler.curChar().toString(), startPos)
        }
    }
    return compiler.setState(::stateAgentMid)
}
