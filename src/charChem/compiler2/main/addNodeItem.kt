package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.core.ChemNodeItem
import charChem.core.ChemSubObj

fun addNodeItem(compiler: ChemCompiler, subObj: ChemSubObj) {
    closeItem(compiler)
    val item = ChemNodeItem(subObj)
    /*
    if (compiler.specMass != 0.0) {
        item.mass = compiler.specMass
        compiler.specMass = 0.0
    }
    item.atomNum = compiler.customAtomNumber
    compiler.customAtomNumber = null
     */
    getNodeForced(compiler).items.add(item)
}