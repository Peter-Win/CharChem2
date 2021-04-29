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
    private var curSubChainId = 0

    private fun createChain(): Int {
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
    }

    private fun mergeSubChains(dstId: Int, srcId: Int, step: Point) {
        if (dstId == srcId) {
            return
        }
        val nodes = subChainsDict[srcId]!!
        nodes.forEach {
            it.subChain = dstId
            it.pt -= step
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

    /**
     * Связь, у которой второй узел указан через ссылку
     */
    fun bondToRef(bond: ChemBond) {
        val (srcNode, dstNode) = bond.nodes
        if (srcNode == null || dstNode == null) {
            return
        }
        // Если узлы принадлежат разным цепям, то нужно срастить две цепи
        mergeChains(srcNode, dstNode)
        val srcSubChain = srcNode.subChain
        val dstSubChain = dstNode.subChain
        if (srcSubChain != dstSubChain) {
            // Если разные подцепи соединяются мягкой связью, то они остаются разными
            // иначе подцепи сращиваются
            if (!bond.soft) {
                mergeSubChains(dstSubChain, srcSubChain, bond.dir!!)
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
        }
    }

    fun closeChain() {
        curChainId = 0
        curSubChainId = 0
    }
    fun closeSubChain() {
        curSubChainId = 0
    }

    fun findNode(pt: Point): ChemNode? {
        return getCurSubChain().find { it.pt == pt }
    }
}