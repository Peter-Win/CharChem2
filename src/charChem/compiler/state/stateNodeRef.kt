package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.isSpace
import charChem.compiler.parse.scanId
import charChem.compiler.parse.scanInt
import charChem.core.ChemAtom
import charChem.core.ChemNode
import charChem.core.findElement

fun onReferenceError(compiler: ChemCompiler, ref: String, pos: Int): Nothing {
    compiler.error("Invalid node reference '[ref]'", listOf("ref" to ref, "pos" to pos))
}

fun useRef(compiler: ChemCompiler, node: ChemNode) {
    val curBond = compiler.curBond
    if (curBond != null) {
        curBond.soft = false
        curBond.nodes[1] = node
        compiler.chainSys.bondToRef(curBond)
    } else {
        compiler.chainSys.addNode(node)
        compiler.nodesBranch.onNode(node)
    }
    compiler.curNode = node
}

fun useRefByNumber(compiler: ChemCompiler, n: Int, startPos: Int) {
    val nodes: List<ChemNode> = compiler.curAgent!!.nodes
    val index: Int = if (n < 0) nodes.size + n else n - 1
    if (index < 0 || index >= nodes.size) {
        onReferenceError(compiler, n.toString(), startPos)
    }
    useRef(compiler, nodes[index])
}

private fun isAtomNode(node: ChemNode, atom: ChemAtom): Boolean =
        node.items.size == 1 && node.items[0].obj == atom

private fun useRefByAtom(compiler: ChemCompiler, atom: ChemAtom, startPos: Int) {
    val nodes: List<ChemNode> = compiler.curAgent!!.nodes
    useRef(compiler, nodes.find { isAtomNode(it, atom) } ?: onReferenceError(compiler, atom.id, startPos))
}

fun useRefById(compiler: ChemCompiler, id: String, startPos: Int) {
    compiler.references[id]?.let {
        useRef(compiler, it)
    } ?: findElement(id)?.let {
        useRefByAtom(compiler, it, startPos)
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
        scanId(compiler)?.let { id -> useRefById(compiler, id, startPos) }
                ?: onReferenceError(compiler, compiler.curChar().toString(), startPos)

    }
    return compiler.setState(::stateAgentMid)
}
