package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.MiddlePoint
import charChem.compiler.parse.scanArgs
import charChem.core.ChemBond
import charChem.math.Point

/*
Средние точки ставятся перед описанием связи.
В итоге общий вектор связи является суммой средних точек и самой связи
Связь, имеющая средние точки, не может быть мягкой
 */

fun applyMiddlePoints(compiler: ChemCompiler, bond: ChemBond) {
    val middlePoints = compiler.middlePoints
    if (middlePoints.isEmpty()) {
        return
    }
    val bondPoints = mutableListOf<Point>()
    bondPoints.addAll(middlePoints.map { it.pt })
    bondPoints.add(bond.dir!!)
    bond.middlePoints = bondPoints
    bond.dir = bondPoints.reduce { acc, pt -> acc + pt }
    bond.soft = false
    middlePoints.clear()
}

fun checkMiddlePoints(compiler: ChemCompiler) {
    if (compiler.middlePoints.isNotEmpty()) {
        compiler.error("Invalid middle point", listOf("pos" to compiler.middlePoints[0].pos))
    }
}

fun createMiddlePoint(compiler: ChemCompiler) {
    // compiler.curChar() == 'm'
    val startPos = compiler.pos - 1
    compiler.pos++
    if (compiler.curChar() != '(') {
        compiler.error("Expected '(' after [S]", listOf(
                "pos" to compiler.pos - 1,
                "S" to "_m",
        ))
    }
    getNodeForced(compiler, true)
    compiler.pos++
    val args = scanArgs(compiler)
    val params = makeParamsDict(args.args, args.argPos)
    val dir = calcBondDirection(compiler, params)
    compiler.middlePoints.add(MiddlePoint(dir, startPos))
}