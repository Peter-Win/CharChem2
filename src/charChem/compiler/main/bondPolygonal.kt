package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.scanBondSuffix
import charChem.core.ChemBond

fun createPolygonalBond(compiler: ChemCompiler) {
    val beginPos = compiler.pos - 1
    val mode: Char = compiler.curChar()
    var multiplicity = 1
    val sign: Int = when (mode) {
        'p' -> 1
        'q' -> -1
        // Эта ошибка не может возникнуть из-за ошибки пользователя. Только если ошибка в компиляторе.
        else -> compiler.error("Invalid polygonal bond descriptor [c]",
                listOf("c" to mode))
    }
    compiler.pos++
    // Далее возможен повторный символ, который означает двойную связь
    if (compiler.curChar() == mode) {
        multiplicity++
        compiler.pos++
    }
    // Далее возможно указание количество углов полигона (Если не указано, то 5)
    var strCount = ""
    while (compiler.curChar() in '0'..'9') {
        strCount += compiler.curChar()
        compiler.pos++
    }
    val count = sign * (strCount.toIntOrNull() ?: 0)

    val bond = createCommonBond(compiler)
    bond.n = multiplicity.toDouble()
    bond.dir = createPolygonStep(compiler, if (count == 0) 5 else count, compiler.varLength)

    scanBondSuffix(compiler, bond)
    bond.tx = compiler.subStr(beginPos)

    onOpenBond(compiler, bond)
}