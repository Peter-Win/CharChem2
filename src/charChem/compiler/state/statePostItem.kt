package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.scanCoeff
import charChem.compiler.scanOxidation

fun statePostItem(compiler: ChemCompiler): Int {
    val oxid = scanOxidation(compiler)
    if (oxid != null) {
        compiler.getLastItem()!!.charge = oxid
        return 0
    }

    val k = scanCoeff(compiler)
    if (k != null) {
        compiler.getLastItem()!!.n = k
        return 0
    }

    return compiler.setState(::stateAgentMid)
}