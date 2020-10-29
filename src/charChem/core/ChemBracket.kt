package charChem.core

// Pairs of open and closed brackets
val bracketPairs: Map<String, String> = mapOf("(" to ")", "[" to "]", "{[" to "]}")

val bracketEnds: List<String> = bracketPairs.map { it.value }

class ChemBracketBegin(val text: String) : ChemObj() {
    var end: ChemBracketEnd? = null
    // this.nodes = [null, null]
    // this.bond = null
    override fun walk(visitor: Visitor) {
        visitor.bracketBegin(this)
    }
}

class ChemBracketEnd(val text: String, val begin: ChemBracketBegin) : ChemObj(), ChemChargeOwner {
    override var charge: ChemCharge? = null
    var n: ChemK = ChemK(1)
    // this.nodes = [null, null]
    // this.bond = null
    override fun walk(visitor: Visitor) {
        visitor.bracketEnd(this)
    }
}