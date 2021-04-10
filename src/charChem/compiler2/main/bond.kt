package charChem.compiler2.main

import charChem.compiler2.ChemCompiler
import charChem.core.ChemBond

// Множество символов, с которых может начинаться краткое описание связи
val bondStart = setOf<Char>('≡', '–', '-', '=', '%', '/', '\\');

// 0=(кратность), 1=(угол в градусах), 2=(знак наклона), 3=(признак мягкой связи), 4=(текст)
class BondDef(val n: Int, val angle: Int, val slope: Int, val soft: Boolean = false, val text: String? = null) {
    fun create(bondId: String): ChemBond {
        val bond = ChemBond()
        bond.tx = text ?: bondId
        bond.n = n.toDouble()
        bond.soft = soft
        return bond
    }
}

val soft1 = BondDef(1, 0, 0, true, "-")
val soft3 = BondDef(3, 0, 0, true, "≡")

val bondDefDict = mapOf<String, BondDef>(
    "-" to soft1,
    "–" to soft1, // special character u2013
    "=" to BondDef(2, 0, 0, true),
    "%" to soft3,
    "≡" to soft3,
    "--" to BondDef(1, 0, 0, false, "-"),
    "==" to BondDef(2, 0, 0, false, "="),
    "%%" to BondDef(3, 0, 0, false, "≡"),
    "|" to  BondDef(1, 90, 0),
    "||" to BondDef(2, 90, 0),
    "|||" to BondDef(3, 90, 0),
    "/" to BondDef(1, 0, -1),
    "//" to BondDef(2, 0, -1),
    "///" to BondDef(3, 0, -1),
    "\\" to BondDef(1, 0, 1),
    "\\\\" to BondDef(2, 0, 1),
    "\\\\\\" to BondDef(3, 0, 1),
)

fun scanSimpleBond(compiler: ChemCompiler): ChemBond? {
    var bondId = ""
    var bondDef: BondDef? = null
    while (true) {
        val curBondId = bondId + compiler.curChar()
        // Постепенно увеличиваем длину описания
        // Как только получается несуществующее описание, то закончить цикл
        val curBondDef = bondDefDict[curBondId] ?: return bondDef?.create(bondId)
        bondId = curBondId
        bondDef = curBondDef
        compiler.pos++
    }
}