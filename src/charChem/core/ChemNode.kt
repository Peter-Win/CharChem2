package charChem.core

import charChem.math.Point

fun isEmptyNode(node: ChemNode): Boolean {
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

class ChemNode(var pt: Point = Point()) : ChemObj(), ChemChargeOwner {
    override var charge: ChemCharge? = null
    val items: MutableList<ChemNodeItem> = mutableListOf()
    var index: Int = -1 // index of node in CAgent.nodes array
    var chain: Int = 0  // chain number
    var subChain: Int = 0
    var autoMode: Boolean = false
    var bonds: MutableSet<ChemBond> = mutableSetOf()
    var fixed: Boolean = false

    override fun walk(visitor: Visitor) {
        visitor.nodePre(this)
        if (visitor.isStop) return
        for (it in items) {
            it.walk(visitor)
            if (visitor.isStop) return
        }
        visitor.nodePost(this)
    }
}