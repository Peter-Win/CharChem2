package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.createPolygonalBond
import charChem.compiler.main.createUniversalBond
import charChem.compiler.parse.scanArgs

fun stateUniBond(compiler: ChemCompiler): Int {
    when (compiler.curChar()) {
        '(' -> {
            compiler.pos++
            val args = scanArgs(compiler)
            createUniversalBond(compiler, args.args, args.argPos)
        }
        'p', 'q' ->  createPolygonalBond(compiler)
        'm' -> compiler.error("Middle points is not implemented yet", listOf())
        'o' -> compiler.error("Ring bond is not implemented yet", listOf())
        's' -> compiler.error("Spline bond is not implemented yet", listOf())
        else -> createUniversalBond(compiler, listOf(), listOf())
    }
    return compiler.setState(::stateAgentMid)
}