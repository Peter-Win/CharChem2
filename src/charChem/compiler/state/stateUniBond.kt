package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.*
import charChem.compiler.parse.scanArgs

fun stateUniBond(compiler: ChemCompiler): Int {
    when (compiler.curChar()) {
        '(' -> {
            compiler.pos++
            val args = scanArgs(compiler)
            createUniversalBond(compiler, args.args, args.argPos)
        }
        'p', 'q' ->  createPolygonalBond(compiler)
        'm' -> createMiddlePoint(compiler)
        'o' -> createRingBond(compiler, 1)
        's' -> createSplineBond(compiler)
        else -> createUniversalBond(compiler, listOf(), listOf())
    }
    return compiler.setState(::stateAgentMid)
}