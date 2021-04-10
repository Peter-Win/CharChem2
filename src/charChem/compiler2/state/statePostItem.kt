package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.getLastItem
import charChem.compiler2.parse.scanCoeff

fun statePostItem(compiler: ChemCompiler): Int {
    val k = scanCoeff(compiler)
    if (k != null) {
        getLastItem(compiler)!!.n = k
    }
    return compiler.setState(::stateAgentMid)
}