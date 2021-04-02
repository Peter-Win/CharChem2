package charChem.compiler

import charChem.core.ChemBond
import charChem.math.Point
import charChem.math.pointFromRad
import kotlin.math.abs

val defaultSlope: Double = Math.PI / 6.0 // 30° - стандартный наклон кратких описаний связей
val correctedSlope = Math.PI / 3.0

// Автокоррекция. Шаг 1.
// Не используется, если угол наклона явно указан при помощи $slope

fun autoCorrection1(compiler: ChemCompiler, bond: ChemBond, def: BondDef) {
    val prevBond = compiler.prevBond
    var needCorrect = false
    var id1: Int = 0
    var id2: Int = 0
    if (compiler.userSlope == 0.0 && prevBond != null && prevBond.isAuto) {
        id1 = bondSlopeId(prevBond)
        id2 = calcSlopeId(bond.slope, bond.isNeg, def.isHoriz(), false)
        needCorrect = ((id1 == 3 || id1 == 9) && bond.slope != 0.0) || // horiz + slope
                ((id1 == 8 || id1 == 7) && id2 == 4) ||     // `/\
                ((id1 == 4 || id1 == 5) && id2 == 8) ||		// \`/
                ((id1 == 10 || id1 == 11) && id2 == 2) ||	// `\/
                ((id1 == 1 || id1 == 2) && id2 == 10)		//  /`\
    }
    val slope: Double = if (needCorrect) correctedSlope else {
        if (compiler.userSlope != 0.0) compiler.userSlope else defaultSlope
    }
    var angle = def.angle + def.slopeSign * slope
    if (bond.isNeg) {
       angle += Math.PI
    }
    bond.dir = pointFromRad(angle)
    bond.isCorr = needCorrect

    // Коррекция предыдущей связи:
    // Коррекция разрешена, есть предыдущая связь, она автоматическая и не корректировалась
    val node = bond.nodes[0]
    if (compiler.userSlope == 0.0 && prevBond != null && prevBond.isAuto && !prevBond.isCorr && node != null && !node.fixed) {
        // Либо сочетание наклонной связи с горизонтальной.
        // Либо сочетание разнонаправленных наклонных связей
        if (((id1==4 || id1==5) && id2==8) ||  // \`/
                ((id1==2 || id1==1) && id2==10) || // /`\
                ((id1==10 || id1==11) && id2==2) || // `\/
                ((id1==8 || id1==7) && id2==4) || // `/\
                ((id1==10 || id1==8 || id1==2 || id1==4) && def.isHoriz())
        ) {
            val a = prevBond.nodes[0]!!.pt
            val d = node.pt - a
            val sx = if (d.x < 0.0) -1.0 else 1.0
            val sy = if (d.y < 0.0) -1.0 else 1.0
            val d1 = Point(abs(d.y) * sx, abs(d.x) * sy)
            val newPt = d1 + a
            // val corr = newPt - node.pt
            prevBond.isCorr = true
            print("autoCorrection old=${node.pt}, new=$newPt\n")
            node.pt = newPt
        }

    }
}
