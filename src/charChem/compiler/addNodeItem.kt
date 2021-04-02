package charChem.compiler

import charChem.core.ChemNode
import charChem.core.ChemNodeItem
import charChem.core.ChemSubObj
import charChem.inspectors.makeTextFormula

fun addNodeItem(compiler: ChemCompiler, subObj: ChemSubObj) {
    print("addNodeItem ${makeTextFormula(subObj)}\n")
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