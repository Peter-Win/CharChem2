package charChem.compiler

import charChem.core.ChemBond
import charChem.math.is0

/**
 * 	Строим числовые характеристики связей. Они соответствуют циферблату:
 * неоткорректированные наклонные ближе к горизонтали (2,4,8,10)
 * откорректированные ближе к вертикали (1,5,7,11)
 * 1=откорректированная связь /, 2=неоткорректированная связь /,
 * 3= -, 4=не корректированная \, 5= корректированная \
 * 6= |, 7= не корректированная `/, 8=корректированная `/, 9= `-,
 * 10= не корректированная `\, 11 = корректированная `\, 12 = `|
 */
fun calcSlopeId(slope: Double, bNeg: Boolean, bHoriz: Boolean, bCorr: Boolean): Int {
    if (slope == 0.0) { // Либо горизонтальная, либо вертикальная
        return if (bHoriz) {
            if (bNeg) 9 else 3
        } else {
            if (bNeg) 12 else 6
        }
    }
    // остаются наклонные, у которых slope 1 или -1
    if (slope > 0.0) { // 4,5,10,11
        return if (bCorr) {
            if (bNeg) 11 else 5
        } else {
            if (bNeg) 10 else 4
        }
    }
    // 1,2, 7,8
    return if (bCorr) {
        if (bNeg) 7 else 1
    } else {
        if (bNeg) 8 else 2
    }
}

fun bondSlopeId(bond: ChemBond): Int =
        calcSlopeId(bond.slope, bond.isNeg, bond.dir?.let { is0(it.y) } ?: true, bond.isCorr)
