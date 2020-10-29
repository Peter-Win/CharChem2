package charChem.core

class ChemComma() : ChemObj() {
    override fun walk(visitor: Visitor) {
        visitor.comma(this)
    }
}