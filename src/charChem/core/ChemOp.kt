package charChem.core

// Операция в химическом выражении
// div - is divide expression by parts. =, -> are dividers. + is not divider

class ChemOp(val srcText: String, val dstText: String, val div: Boolean) : ChemObj() {
    var commentPre: ChemComment? = null
    var commentPost: ChemComment? = null
    var color: String? = null
    override fun walk(visitor: Visitor) {
        visitor.operation(this)
    }
}