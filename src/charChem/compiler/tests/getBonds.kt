package charChem.compiler.tests

import charChem.core.ChemBond
import charChem.core.ChemObj
import charChem.core.Visitor

fun getBonds(expr: ChemObj): List<ChemBond> {
    val bonds = mutableListOf<ChemBond>()
    expr.walk(object : Visitor() {
        override fun bond(obj: ChemBond) {
            bonds.add(obj)
        }
    })
    return bonds
}