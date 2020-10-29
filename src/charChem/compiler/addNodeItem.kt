package charChem.compiler

import charChem.core.ChemNode
import charChem.core.ChemNodeItem
import charChem.core.ChemSubObj

fun addNodeItem(compiler: ChemCompiler, subObj: ChemSubObj) {
    compiler.closeItem()
    val item = ChemNodeItem(subObj)
    if (compiler.specMass != 0.0) {
        item.mass = compiler.specMass
        compiler.specMass = 0.0
    }
    item.atomNum = compiler.customAtomNumber
    compiler.customAtomNumber = null
    compiler.getForcedNode().items.add(item)
}