package charChem.inspectors

import charChem.core.ChemBond
import charChem.core.ChemObj
import charChem.core.Visitor

class IsNonText: Visitor() {
    override fun bond(obj: ChemBond) {
        isStop = !obj.isText
    }
}

fun isTextFormula(obj: ChemObj): Boolean {
    val visitor = IsNonText()
    obj.walk(visitor)
    return !visitor.isStop
}