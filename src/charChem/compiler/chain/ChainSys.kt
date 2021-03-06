package charChem.compiler.chain

import charChem.compiler.ChemCompiler
import charChem.core.ChemBond
import charChem.core.ChemNode
import charChem.math.Point

var baseChainId = 1
fun generateChainId(): Int = baseChainId++

var baseSubChainId = 1
fun generateSubChainId(): Int = baseSubChainId++

class ChainSys(val compiler: ChemCompiler) {
    private val chainsDict: MutableMap<Int, MutableSet<Int>> = mutableMapOf()
    private val subChainsDict: MutableMap<Int, MutableList<ChemNode>> = mutableMapOf()

    private var curChainId = 0
    var curSubChainId = 0
    private var lastBond: ChemBond? = null
    private val stack: MutableList<ChemBond?> = mutableListOf()

    fun getLastBond(): ChemBond? = lastBond

    fun onBranchBegin() {
        stack.add(0, lastBond)
    }

    fun onBranchEnd() {
        lastBond = stack.removeAt(0)
    }

    private fun createChain(): Int {
        lastBond = null
        val newChainId = generateChainId()
        chainsDict[newChainId] = mutableSetOf()
        return newChainId
    }

    private fun getCurChain(): MutableSet<Int> {
        if (curChainId == 0) {
            curChainId = createChain()
        }
        return chainsDict[curChainId]!!
    }

    private fun createSubChain(): Int {
        lastBond = null
        val newId = generateSubChainId()
        curSubChainId = newId
        subChainsDict[newId] = mutableListOf()
        getCurChain().add(newId)
        return newId
    }

    private fun getCurSubChain(): MutableList<ChemNode> {
        if (curSubChainId == 0) {
            curSubChainId = createSubChain()
        }
        return subChainsDict[curSubChainId]!!
    }

    fun addNode(node: ChemNode) {
        if (node.chain == 0) {
            getCurSubChain().add(node)
            node.chain = curChainId
            node.subChain = curSubChainId
        } else {
            curChainId = node.chain
            curSubChainId = node.subChain
        }
    }

    fun setCurNode(node: ChemNode) {
        curChainId = node.chain
        curSubChainId = node.subChain
    }

    fun addBond(bond: ChemBond) {
        if (bond.soft) {
            createSubChain()
        }
        lastBond = bond
    }

    private fun mergeSubChains(dstId: Int, srcId: Int, step: Point) {
        if (dstId == srcId) {
            return
        }
        val nodes = subChainsDict[srcId]!!
        nodes.forEach {
            it.subChain = dstId
            it.pt += step
        }
        subChainsDict[dstId]!!.addAll(nodes)
        subChainsDict.remove(srcId)
        if (curSubChainId == srcId) {
            curSubChainId = dstId
        }
    }

    /**
     * scrChain присоединяется к dstChain
     */
    private fun mergeChains(srcNode: ChemNode, dstNode: ChemNode) {
        val srcChainId = srcNode.chain
        val dstChainId = dstNode.chain
        if (srcChainId != dstChainId) {
            compiler.curAgent!!.nodes.filter {
                it.chain == srcChainId
            }.forEach {
                it.chain = dstChainId
            }
        }
    }

    private fun makeTransitionBond(bond: ChemBond) {
        bond.soft = false
        bond.dir = null
        bond.nodes.getOrNull(1)?.let { addNode(it) }
    }

    /**
     * Связь, у которой второй узел указан через ссылку
     */
    fun bondToRef(bond: ChemBond) {
        val (srcNode, dstNode) = bond.nodes
        if (srcNode == null || dstNode == null) {
            return
        }
        // Если узлы принадлежат разным цепям, то нужно срастить две цепи
        val srcSubChain = srcNode.subChain
        val dstSubChain = dstNode.subChain
        if (srcSubChain != dstSubChain) {
            // Если разные подцепи соединяются мягкой связью, то они остаются разными
            // иначе подцепи сращиваются
            if (!bond.soft) {
                // Если цепи разные, то их нужно объединить
                val srcChain = srcNode.chain
                val dstChain = dstNode.chain
                if (srcChain != dstChain) {
                    mergeChains(srcNode, dstNode)
                    val step = dstNode.pt - srcNode.pt - bond.dir!!
                    mergeSubChains(dstSubChain, srcSubChain, step)
                } else {
                    // Если цепь одна, но разные подцепи, то это переходная связь
                    makeTransitionBond(bond)
                }
            }
        } else {
            // Но если узлы в одной подцепи, то корректировать шаг связи
            bond.dir = dstNode.pt - srcNode.pt
        }
        compiler.curNode = dstNode
        curChainId = dstNode.chain
        curSubChainId = dstNode.subChain
    }

    fun changeBondToHard(bond: ChemBond) {
        bond.nodes[0]?.let { dstNode ->
            bond.nodes[1]?.let { srcNode ->
                if (srcNode.chain != dstNode.chain) {
                    throw Error("Different chains")
                }
                mergeSubChains(dstNode.subChain, srcNode.subChain, bond.dir ?: Point())
            }
            curSubChainId = dstNode.subChain
            curChainId = dstNode.chain
        }
    }

    fun closeChain() {
        curChainId = 0
        curSubChainId = 0
        lastBond = null
    }

    fun closeSubChain() {
        curSubChainId = 0
        lastBond = null
    }

    fun findNode(pt: Point): ChemNode? {
        return getCurSubChain().find { it.pt == pt }
    }
}