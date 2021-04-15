package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.core.ChemBond
import charChem.core.ChemNode

fun bindNodeToBond(compiler: ChemCompiler, node: ChemNode, chemBond: ChemBond) {
    compiler.curNode = node

    chemBond.nodes[1] = node
    val node0 = chemBond.nodes[0]
    val auto0: Boolean = node0?.autoMode ?: false
    // Если оба узла автоматические, то связь не мягкая
    if (chemBond.soft && auto0 && node.autoMode) {
        chemBond.soft = false
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
        nodes.size == 2 && (
            (nodes[0] == nodeA && nodes[1] == nodeB) ||
            (nodes[0] == nodeB && nodes[1] == nodeA)
        )
    }
}

// Предполагается, что свойства bond уже заполнены. В первую очередь: dir, n
fun onOpenBond(compiler: ChemCompiler, bond: ChemBond) {
    val oldNode = compiler.curNode ?: openNode(compiler, true)
    closeNode(compiler)
    bond.nodes[0] = oldNode
    // Здесь можно сделать предположение о том, что связь входит вкакой-либо из узлов своей подцепи
    // Это предположение неверно для случаев:
    // - dir.isZero()
    // - связь мягкая и oldNode не является автоматическим узлом
    val dir = bond.dir
    if (dir != null && !dir.isZero() && !(bond.soft && !oldNode.autoMode)) {
        val pt = bond.calcPt()
        val newNode = compiler.chainSys.findNode(pt)
        newNode?.let {
            if (!bond.soft || newNode.autoMode) {
                findBondBetweenNodes(compiler, oldNode, newNode)?.let { oldBond ->
                    mergeBonds(compiler, oldBond, bond, newNode)
                    return
                }
                bindNodeToBond(compiler, newNode, bond)
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
}