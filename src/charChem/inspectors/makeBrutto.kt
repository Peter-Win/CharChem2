package charChem.inspectors

import charChem.core.*

fun makeBrutto(expr: ChemObj): ChemExpr {
    val result = ChemExpr()
    val agent = ChemAgent()
    result.entities.add(agent)
    val node = agent.addNode(ChemNode())
    val elemList = makeElemList(expr)
    elemList.sortByHill()
    elemList.list.forEach{ elemRec ->
        val item = elemRec.elem ?: ChemCustom(elemRec.id)
        node.items.add(ChemNodeItem(item, ChemK(elemRec.n)))
    }
    return result
}