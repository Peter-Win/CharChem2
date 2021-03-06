package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemK
import charChem.core.ChemNode
import charChem.core.ChemNodeItem
import charChem.core.PeriodicTable
import kotlin.math.roundToInt

fun closeNode(compiler: ChemCompiler) {
    closeItem(compiler)
    compiler.curNode = null
    compiler.chargeOwner = null
}

fun openNode(compiler: ChemCompiler, isAuto: Boolean = false): ChemNode {
    compiler.curBond?.let { bond ->
        val dir = bond.dir
        if (bond.soft && isAuto) {
            // if second node of bond is auto, then bond is not soft
            changeBondToHard(compiler, bond)
        }
        if (dir != null && !dir.isZero() && !bond.soft) {
            val pt = bond.calcPt()
            compiler.chainSys.findNode(pt)?.let { existsNode ->
                compiler.nodesBranch.onNode(existsNode)
                if (!bond.soft || existsNode.autoMode) {
                    if (bond.middlePoints == null) {
                        val oldNode = bond.nodes[0]!!
                        findBondBetweenNodes(compiler, oldNode, existsNode)?.let { oldBond ->
                            mergeBonds(compiler, oldBond, bond, existsNode)
                            return existsNode
                        }
                    }
                }
                bindNodeToBond(compiler, existsNode, bond)
                return existsNode
            }
        }
    }
    closeNode(compiler)
    checkMiddlePoints(compiler)
    val node = compiler.curAgent!!.addNode(ChemNode())
    node.index = compiler.curAgent!!.nodes.size - 1
    node.autoMode = isAuto
    compiler.curNode = node
    compiler.chargeOwner = node
    compiler.chainSys.addNode(node)
    compiler.nodesBranch.onNode(node)

    bindNodeToCurrentBond(compiler, node)
    return node
}

fun getNodeForced(compiler: ChemCompiler, isAuto: Boolean): ChemNode {
    return compiler.curNode ?: openNode(compiler, isAuto)
}

// Вызывается в самом конце, когда уже заполнен список bonds
fun updateAutoNode(node: ChemNode) {
    node.items.add(ChemNodeItem(PeriodicTable.C))
    val multipleSum: Double = node.bonds.fold(0.0) { sum, chemBond -> sum + chemBond.n }
    val countH: Int = 4 - multipleSum.roundToInt()
    if (countH > 0) {
        node.items.add(ChemNodeItem(PeriodicTable.H, ChemK(countH)))
    }
}