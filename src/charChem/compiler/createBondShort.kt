package charChem.compiler

import charChem.core.ChemBond
import charChem.math.is0
import charChem.math.pointFromRad
import kotlin.math.abs

/**
 * Создание связи из краткого описания
 */
fun createBondShort(compiler: ChemCompiler): ChemBond {
    val def = findBondDef(compiler.text, compiler.pos)
            ?: compiler.error("Invalid bond def", listOf("pos" to compiler.pos))
    compiler.pos += def.descr.length
    val bond = createBond(compiler)
    bond.isAuto = true
    bond.n = def.multiplicity.toDouble()
    bond.tx = def.text ?: def.descr
    bond.isNeg = compiler.isNegChar
    compiler.isNegChar = false

    bond.soft = def.isSoft
    bond.slope = def.slopeSign.toDouble()

    loadSuffix(compiler, bond)

    autoCorrection1(compiler, bond, def)
    bond.checkText()

    return bond
}

fun switchWidth(bond: ChemBond, sign: Int) {
    if (bond.w1 != sign) {
        bond.w0 = 0
        bond.w1 = sign
    } else {
        bond.w0 = sign
        bond.w1 = 0
    }
}

fun switchArrow(bond: ChemBond) {
    if (!bond.arr0 && !bond.arr1) {
        bond.arr1 = true
    } else if (!bond.arr0) {
        bond.arr0 = true
        bond.arr1 = false
    } else {
        bond.arr1 = true
    }
}

fun loadSuffix(compiler: ChemCompiler, bond: ChemBond) {
    while (!compiler.isFinish()) {
        when (compiler.curChar()) {
            '0', 'o' -> bond.n = 0.0 // Пустая связь
            'h' -> bond.setHydrogen()
            'w' -> switchWidth(bond, 1)
            'd' -> switchWidth(bond, -1)
            'x' -> bond.setCross()
            '~' -> bond.style = "~"
            'v' -> switchArrow(bond)
            else -> return
        }
        compiler.pos++
    }
}