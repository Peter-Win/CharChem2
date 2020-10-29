package charChem.core

// Операция в химическом выражении

class ChemOp(val srcText: String, val dstText: String, val eq: Boolean) : ChemObj() {
    var commentPre: ChemComment? = null
    var commentPost: ChemComment? = null
    override fun walk(visitor: Visitor) {
        visitor.operation(this)
    }
}