package charChem.core

import charChem.math.Point

fun isEmptyNode(node: ChemObj): Boolean {
    return !node.walkExt(object : Visitor() {
        override fun atom(obj: ChemAtom) = stop()
        override fun radical(obj: ChemRadical) = stop()
        override fun custom(obj: ChemCustom) {
            isStop = obj.text.isNotEmpty()
        }
        override fun comment(obj: ChemComment) {
            isStop = obj.text.isNotEmpty()
        }
    }).isStop
}

/**
 * Priority table for different items
 * 1 = comment
 * 2 = abstract item
 * 3 = H element
 * 4 = radicals and all elements (except C and H)
 * 5 = C element
 * 6 = item with bCenter flag
 */
enum class ItemPriority {
    NA, Comment, Abstract, Hydrogen, Default, Carbon, Explicit
}

class ChemNode(var pt: Point = Point()) : ChemObj(), ChemChargeOwner {
    override var charge: ChemCharge? = null
    val items: MutableList<ChemNodeItem> = mutableListOf()
    var index: Int = -1 // index of node in CAgent.nodes array
    var chain: Int = 0  // chain number
    var subChain: Int = 0
    var autoMode: Boolean = false
    var bonds: MutableSet<ChemBond> = mutableSetOf()
    var fixed: Boolean = false
    var color: String? = null
    var atomColor: String? = null

    override fun walk(visitor: Visitor) {
        visitor.nodePre(this)
        if (visitor.isStop) return
        for (it in items) {
            it.walk(visitor)
            if (visitor.isStop) return
        }
        visitor.nodePost(this)
    }
    
    fun addBond(bond: ChemBond) = bonds.add(bond)

    fun getCenterItem(): ChemNodeItem? {
        var curPriority = ItemPriority.NA
        var maxPriority = ItemPriority.NA
        var foundItem: ChemNodeItem? = null
        walk(object : Visitor() {
            override fun itemPre(obj: ChemNodeItem) {
                curPriority = ItemPriority.NA
            }
            override fun comment(obj: ChemComment) {
                curPriority = ItemPriority.Comment
            }
            override fun custom(obj: ChemCustom) {
                curPriority = ItemPriority.Abstract
            }
            override fun radical(obj: ChemRadical) {
                curPriority = ItemPriority.Default
            }
            override fun atom(obj: ChemAtom) {
                curPriority = when (obj.id) {
                    "H" -> ItemPriority.Hydrogen
                    "C" -> ItemPriority.Carbon
                    else -> ItemPriority.Default
                }
            }
            override fun itemPost(obj: ChemNodeItem) {
                if (obj.bCenter) {
                    curPriority = ItemPriority.Explicit
                }
                if (curPriority > maxPriority) {
                    maxPriority = curPriority
                    foundItem = obj
                }
            }
        })
        return foundItem
    }
}