package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemBond
import charChem.math.Point
import charChem.math.is0

val defaultSlope: Double = Math.PI / 6.0 // 30° - стандартный наклон кратких описаний связей
val correctedSlope = Math.PI / 3.0


// -1, `-1, 0, 0, 1, `1
val horizAngles = listOf<Double>(-60.0, 120.0, 0.0, 0.0, 60.0, -120.0)
fun newAngleDeg(bond: ChemBond): Double = horizAngles[(bond.slope + 1) * 2 + if (bond.isNeg) 1 else 0]

fun correct(bond: ChemBond, length: Double?) {
    bond.dir = makeBondStep(newAngleDeg(bond), length ?: bond.dir!!.length())
    bond.isCorr = true
    bond.nodes[1]?.let { it.pt = bond.calcPt() }
}

fun correctPrev(compiler: ChemCompiler, prevBond: ChemBond, length: Double?) {
    val corrNode = prevBond.nodes[1]
    if (corrNode == null) {
        correct(prevBond, length)
        return
    }
    val subChain = corrNode.subChain
    val oldPos = corrNode.pt
    correct(prevBond, length)
    val step = corrNode.pt - oldPos
    val allNodes = compiler.curAgent!!.nodes
    val dstNodes = allNodes.subList(corrNode.index + 1, allNodes.size).filter { it.subChain == subChain }
    dstNodes.forEach { it.pt += step }
}

fun getLastBond(compiler: ChemCompiler): ChemBond? {
    // compiler.curBond
    return compiler.chainSys.getLastBond()
}

fun autoCorrection(compiler: ChemCompiler, bond: ChemBond, slopeSign: Int) {
    if (compiler.varSlope != 0.0) {
        // Если указан угол наклона при помощи $slope(x)
        return
    }
    // Если нет предыдущей связи, то коррекция невозможна
    val prevBond = getLastBond(compiler) ?: return
    if (!prevBond.isAuto) {
        // Коррекция возможно только если предыдущая связь создана из простого описания
        return
    }

    val dir = prevBond.dir
    if (prevBond.isAuto && dir!=null && is0(dir.y) && slopeSign != 0) {
        // Стыковка горизонтальной связи с наклонной
        correct(bond, compiler.varLength)
        return
    }
    if (prevBond.slope != 0 && prevBond.isCorr && slopeSign != 0 && prevBond.isNeg != bond.isNeg) {
        // Стыковка предыдущей откорректированной наклонной связи
        // с тем же наклоном, но в обратном направлении
        correct(bond, compiler.varLength)
        return
    }
    // Варианты с коррекцией предыдущей связи
    if (
            prevBond.slope != 0 &&
            !prevBond.isCorr &&
            slopeSign != 0 &&
            prevBond.isNeg != bond.isNeg &&
            prevBond.slope != bond.slope
    ) {
        correctPrev(compiler, prevBond, null)
        correct(bond, compiler.varLength)
        return
    }
    // Стыковка с горизонтальной связью
    if (prevBond.slope != 0 && !prevBond.isCorr && bond.isHorizontal()) {
        correctPrev(compiler, prevBond, null)
    }
}