package charChem.compiler

import charChem.core.ChemK

// Попытка извлечь коэффициент
// Если коэффициент распознан, возвращается число или строка(для абстрактного коэфф), pos указыв на следующий символ
// В случае неудачи возвращает null

fun scanCoeff(compiler: ChemCompiler): ChemK? {
    var pos0 = compiler.pos
    var ch: Char = compiler.text[compiler.pos]
    if (ch in '0'..'9') {
        // Числовой коэфф
        compiler.pos++
        while (!compiler.isFinish()) {
            ch = compiler.text[compiler.pos]
            if (ch !in '0'..'9') break
            compiler.pos++
        }
        val s = compiler.subStr(pos0)
        return ChemK(s.toDouble())
    }
    if (ch == '\'') {
        // Абстрактный коэфф.
        compiler.pos++
        if (!compiler.scanTo('\''))
            compiler.error("Abstract coefficient is not closed", listOf("pos" to pos0))
        val s = compiler.subStr(pos0 + 1)
        compiler.pos++
        return ChemK(s)
    }
    return null
}