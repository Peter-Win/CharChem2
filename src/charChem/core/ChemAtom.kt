package charChem.core

import kotlin.math.floor

class ChemAtom(
        val n: Int,     // Atomic number
        val id: String, // Symbol of a chemical element: H, He, Li, Be...
        val mass: Double // Atomic mass in Daltons
): ChemSubObj() {
    override fun walk(visitor: Visitor) {
        visitor.atom(this)
    }

    // True for element whose isotopes are all radioactive, none of which is stable.
    fun isUnstable() = floor(mass) == mass
}