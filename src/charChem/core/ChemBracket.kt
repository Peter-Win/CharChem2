package charChem.core

// Pairs of open and closed brackets
val bracketPairs: Map<String, String> = mapOf("(" to ")", "[" to "]", "{[" to "]}")

val bracketEnds: List<String> = bracketPairs.map { it.value }

class ChemBracketBegin(val text: String) : ChemObj() {
    var end: ChemBracketEnd? = null
    var isText: Boolean? = null
    var bond: ChemBond? = null
    var color: String? = null
    override fun walk(visitor: Visitor) {
        visitor.bracketBegin(this)
    }
}

class ChemBracketEnd(
        val text: String,
        val begin: ChemBracketBegin,
        val nodeIn: ChemNode
        ) : ChemObj(), ChemChargeOwner
{
    override var charge: ChemCharge? = null
    var n: ChemK = ChemK(1)
    var bond: ChemBond? = null
    // this.nodes = [null, null]
    // this.bond = null
    override fun walk(visitor: Visitor) {
        visitor.bracketEnd(this)
    }
}