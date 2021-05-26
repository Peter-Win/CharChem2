package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemBond
import charChem.core.ChemBracketEnd
import charChem.core.ChemNode
import charChem.inspectors.makeTextFormula
import kotlin.math.roundToInt

fun createCommonBond(compiler: ChemCompiler): ChemBond {
    val bond = ChemBond()
    bond.color = compiler.varColor
    return bond
}

fun bindNodeToBond(compiler: ChemCompiler, node: ChemNode, chemBond: ChemBond) {
    compiler.curNode = node

    chemBond.nodes[1] = node
    val node0 = chemBond.nodes[0]
    val auto0: Boolean = node0?.autoMode ?: false
    // Если хотя бы один узел автоматический, то связь не мягкая
    if (chemBond.soft && (auto0 || node.autoMode)) {
        chemBond.soft = false
        compiler.chainSys.changeBondToHard(chemBond)
    }
    if (chemBond.soft) {
        compiler.nodesBranch.onSubChain()
        compiler.nodesBranch.onNode(node)
    }
    compiler.curBond = null
    // Для жесткой связи можно вычислить координаты второго узла относительно первого
    val dir = chemBond.dir
    if (dir != null && node0 != null && !chemBond.soft && !dir.isZero()) {
        node.pt = node0.pt + dir
    }
}

fun bindNodeToCurrentBond(compiler: ChemCompiler, node: ChemNode) {
    compiler.curBond?.let { bindNodeToBond(compiler, node, it) }
}

fun findBondBetweenNodes(compiler: ChemCompiler, nodeA: ChemNode, nodeB: ChemNode): ChemBond? {
    return compiler.curAgent!!.bonds.find {
        val nodes = it.nodes
        it.middlePoints == null && nodes.size == 2 && (
            (nodes[0] == nodeA && nodes[1] == nodeB) ||
            (nodes[0] == nodeB && nodes[1] == nodeA)
        )
    }
}

fun getNodeForBondStart(compiler: ChemCompiler, bond: ChemBond): ChemNode {
    val curNode = compiler.curNode
    if (curNode != null) {
        // Если текущий узел есть, то использовать его
        return curNode
    }
    // Возможна ситуация, когда связь стыкуется к ранее закрытой скобке
    val bracketEnd: ChemBracketEnd? = compiler.curAgent!!.commands.lastOrNull() ?.let {
        if (it is ChemBracketEnd) it else null
    }
    if (bracketEnd != null) {
        bracketEnd.bond = bond
        return  bracketEnd.nodeIn
    }
    return openNode(compiler, true)
}

// Предполагается, что свойства bond уже заполнены. В первую очередь: dir, n, soft
fun onOpenBond(compiler: ChemCompiler, bond: ChemBond) {
    val oldNode = getNodeForBondStart(compiler, bond)
    closeNode(compiler)
    applyMiddlePoints(compiler, bond)
    if (bond.n == 2.0 && bond.align == null) {
        bond.align = compiler.varAlign
    }
    bond.nodes[0] = oldNode
    if (bond.isAuto && oldNode.autoMode) {
        // Если первый узел простой связи является автоматическим, то связь не мягкая
        bond.soft = false
    }
    bond.color = compiler.varColor
    // Здесь можно сделать предположение о том, что связь входит вкакой-либо из узлов своей подцепи
    // Это предположение неверно для случаев:
    // - dir.isZero()
    // - связь мягкая и oldNode не является автоматическим узлом
    val dir = bond.dir
    if (dir != null && !dir.isZero() && !(bond.soft && !oldNode.autoMode)) {
        val pt = bond.calcPt()
        compiler.chainSys.findNode(pt)?.let { existsNode ->
            compiler.nodesBranch.onNode(existsNode)
            if (!bond.soft || existsNode.autoMode) {
                if (bond.middlePoints == null) {
                    findBondBetweenNodes(compiler, oldNode, existsNode)?.let { oldBond ->
                        mergeBonds(compiler, oldBond, bond, existsNode)
                        return
                    }
                }
                bindNodeToBond(compiler, existsNode, bond)
            }
        }
    }
    compiler.curAgent!!.addBond(bond)
    compiler.curBond = bond
    compiler.chainSys.addBond(bond)
}

fun mergeBonds(compiler: ChemCompiler, oldBond: ChemBond, newBond: ChemBond, newNode: ChemNode) {
    // При наложении связей от новой только добавляется кратность.
    // Остальные характеристики значения не имеют
    oldBond.n += newBond.n
    compiler.curNode = newNode
    compiler.curBond = oldBond
    // TODO: Возможно, здесь стоило бы пометить newBond, что его нельзя корректировать
    newBond.soft = false
    compiler.chainSys.addBond(newBond)
}