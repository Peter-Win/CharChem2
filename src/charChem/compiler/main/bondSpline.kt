package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.ArgsInfo
import charChem.compiler.parse.scanArgs
import charChem.core.ChemNode

private fun nodesInterval(compiler: ChemCompiler, refs: List<String>, pos: Int): List<ChemNode> {
    val nodeA = findNodeEx(compiler, refs[0], pos)
    val nodeB = findNodeEx(compiler, refs[1], pos + refs[0].length + 1)
    val first = nodeA.index.coerceAtMost(nodeB.index)
    val last = nodeA.index.coerceAtLeast(nodeB.index)
    return compiler.curAgent!!.nodes.subList(first, last + 1)
}

/**
 * Список вершин, которые будут включены в сплайновую связь
 * Разделитель - точка с запятой
 * Можно указать интервал через двоеточие
 * Пример: #1:4;6
 */
fun parseNodesListDef(compiler: ChemCompiler, value: String, valuePos: Int): MutableList<ChemNode?>? {
    if (value.isEmpty()) {
        return null
    }
    val chunks = value.split(';')
    var curPos = valuePos
    val nodes: MutableList<ChemNode> = chunks.fold(mutableListOf()) {
        list, chunk ->
        val refs = chunk.split(':')
        if (refs.size == 1) {
            list.add(findNodeEx(compiler, chunk, curPos))
        } else {
            list.addAll(nodesInterval(compiler, refs, curPos))
        }
        curPos += chunk.length + 1
        list
    }
    return nodes.toMutableList()
}

private data class NodesList(val nodes: MutableList<ChemNode?>, val isCycle: Boolean = false)

private fun autoLocateNodes(compiler: ChemCompiler): NodesList =
        findRingNodes(compiler)?.let {
            NodesList(it, true)
        } ?: NodesList(compiler.nodesBranch.nodes.toMutableList(), false)

private fun checkCycledList(nodes: MutableList<ChemNode?>): NodesList =
        if (nodes.size > 1 && nodes[0] == nodes.last()) NodesList(nodes.subList(0, nodes.size - 1), true)
        else NodesList(nodes, false)

fun createSplineBond(compiler: ChemCompiler) {
    // compiler.curChar == 's'
    compiler.pos++
    getNodeForced(compiler, true)
    val args = if (compiler.curChar() != '(') {
        ArgsInfo(listOf(), listOf())
    } else {
        compiler.pos++
        scanArgs(compiler)
    }
    val params = makeParamsDict(args.args, args.argPos)
    val bond = createCommonBond(compiler)
    bond.isCycle = false
    bond.tx = "s"
    setBondProperties(compiler, bond, params)
    val nodes: MutableList<ChemNode?>? = params['#']?.let { param ->
        parseNodesListDef(compiler, param.value, param.valuePos)
    }
    val nodesList: NodesList = nodes?.let { checkCycledList(it) } ?: autoLocateNodes(compiler)
    bond.nodes = nodesList.nodes
    bond.isCycle = ('o' in params) || nodesList.isCycle
    compiler.curAgent!!.addBond(bond)
    compiler.curBond = null
}