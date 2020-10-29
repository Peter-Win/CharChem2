package charChem.compiler

import charChem.core.ChemCharge
import charChem.core.createCharge

// Извлечение заряда из текущей позиции.
// Возвращает объект ChemCharge или null

fun scanCharge(compiler: ChemCompiler, isLeft: Boolean): ChemCharge? {
    if (compiler.isFinish())
        return null
    val pos0 = compiler.pos
    var prevCharge: ChemCharge? = null
    while (true) {
        compiler.pos++
        val charge = createCharge(compiler.subStr(pos0), isLeft)
        if (charge == null) {
            compiler.pos--
            break
        }
        prevCharge = charge
        if (compiler.isFinish())
            break
    }
    return prevCharge
}
