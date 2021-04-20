package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.bindNodeToBond
import charChem.compiler.main.bindNodeToCurrentBond
import charChem.compiler.parse.isSpace
import charChem.compiler.parse.scanId
import charChem.compiler.parse.scanInt
import charChem.core.ChemNode
import charChem.math.Point

fun onReferenceError(compiler: ChemCompiler, ref: String, pos: Int): Nothing {
    compiler.error("Invalid node reference '[ref]'", listOf("ref" to ref, "pos" to pos))
}

fun useRef(compiler: ChemCompiler, node: ChemNode) {
    compiler.curBond?.let { bond ->
        bond.nodes[1] = node
        compiler.chainSys.bondToRef(bond)
        // bindNodeToBond(compiler, node, bond)
    }
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
