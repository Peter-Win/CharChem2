package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemNodeItem
import charChem.core.ChemSubObj

fun addNodeItem(compiler: ChemCompiler, subObj: ChemSubObj) {
    closeItem(compiler)
    val item = ChemNodeItem(subObj)
    if (compiler.varMass != 0.0) {
        item.mass = compiler.varMass
        compiler.varMass = 0.0
    }
    item.atomNum = compiler.varAtomNumber
    compiler.varAtomNumber = null
    getNodeForced(compiler).items.add(item)
}