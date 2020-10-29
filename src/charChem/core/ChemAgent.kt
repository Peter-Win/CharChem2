package charChem.core

class ChemAgent() : ChemObj() {
    val nodes: MutableList<ChemNode> = mutableListOf()
    val bonds: MutableList<ChemBond> = mutableListOf()
    val commands: MutableList<ChemObj> = mutableListOf()
    var n: ChemK = ChemK(1)
    var part: Int = 0
    fun addNode(node: ChemNode): ChemNode {
        nodes.add(node)
        commands.add(node)
        return node
    }

    fun addBond(bond: ChemBond) {
        bonds.add(bond)
        commands.add(bond)
    }

    override fun walk(visitor: Visitor) {
        visitor.agentPre(this)
        if (visitor.isStop) return
        for (cmd in commands) {
            cmd.walk(visitor)
            if (visitor.isStop) return
        }
        visitor.agentPost(this)
    }
}