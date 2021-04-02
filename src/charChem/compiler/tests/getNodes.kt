package charChem.compiler.tests

import charChem.core.ChemNode
import charChem.core.ChemObj
import charChem.core.Visitor

fun getNodes(expr: ChemObj): List<ChemNode> {
    val result = mutableListOf<ChemNode>()
    expr.walk(object: Visitor() {
        override fun nodePre(obj: ChemNode) {
            result.add(obj)
        }
    })
    return result
}