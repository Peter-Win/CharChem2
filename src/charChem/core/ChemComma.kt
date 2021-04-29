package charChem.core

class ChemComma() : ChemSubObj() {
    override fun walk(visitor: Visitor) {
        visitor.comma(this)
    }
}
val instChemComma: ChemComma = ChemComma()
