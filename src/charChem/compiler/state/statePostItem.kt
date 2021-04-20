package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.getLastItem
import charChem.compiler.parse.scanCoeff

fun statePostItem(compiler: ChemCompiler): Int {
    val k = scanCoeff(compiler)
    if (k != null) {
        getLastItem(compiler)!!.n = k
    }
    return compiler.setState(::stateAgentMid)
}