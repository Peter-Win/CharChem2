package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.getLastItem
import charChem.compiler.parse.scanCharge
import charChem.compiler.parse.scanPostItem
import charChem.core.ChemCharge

fun statePostItem(compiler: ChemCompiler): Int {
    val item = getLastItem(compiler) ?: compiler.error("Invalid node", listOf())
    if (scanPostItem(compiler) { item.n = it })
        return compiler.setState(::statePostItem)

    if (compiler.curChar() == '(') {
        val bracketPos = compiler.pos
        compiler.pos++
        val charge: ChemCharge? = scanCharge(compiler, false)
        if (charge != null && compiler.curChar() == ')') {
            item.charge = charge
            return compiler.setState(::statePostItem, 1)
        }
        compiler.pos = bracketPos
    }

    return compiler.setState(::stateAgentMid)
}