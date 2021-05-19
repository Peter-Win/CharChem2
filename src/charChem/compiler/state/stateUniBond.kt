package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.createPolygonalBond
import charChem.compiler.main.createRingBond
import charChem.compiler.main.createSplineBond
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
        'o' -> createRingBond(compiler, 1)
        's' -> createSplineBond(compiler)
        else -> createUniversalBond(compiler, listOf(), listOf())
    }
    return compiler.setState(::stateAgentMid)
}