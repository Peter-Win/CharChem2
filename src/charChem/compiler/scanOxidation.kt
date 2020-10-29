package charChem.compiler

import charChem.core.ChemCharge
import charChem.core.createCharge

// Попытка извлечь из текущей позиции степень окисления
fun scanOxidation(compiler: ChemCompiler): ChemCharge? {
    if (compiler.curChar() != '(') return null
    // Скобка не обязательно служит для описания степени окисления.
    // Поэтому надо извлечь содержимое скобки и проверить, заряд это или нет.
    val pos0 = compiler.pos
    if (!compiler.scanTo(')'))
        compiler.error("It is necessary to close the bracket", listOf("pos" to pos0))
    val charge = createCharge(compiler.subStr(pos0 + 1), false)
    if (charge == null)
        compiler.pos = pos0
    else
        compiler.pos++
    return charge
}