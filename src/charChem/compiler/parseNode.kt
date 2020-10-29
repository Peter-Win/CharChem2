package charChem.compiler

import charChem.compiler.state.*
import charChem.core.ChemComment

// Распознать начало очередного узла в описании реагента.
// Если нет, вернуть -1
fun parseNode(compiler: ChemCompiler): Int {
    val c = compiler.curChar()
    // print("parseNode. c=$c\n")
    val pos = compiler.pos
    // Chemical element
    if (c in 'A'..'Z') {
        // Извлечь первый заглавный символ элемента. Следующие должны быть маленькими
        compiler.elemStartPos = compiler.pos
        return compiler.setState(::stateElement, 1)
    }
    // Признак изменения поведения следующей конструкции
    if (c == '`') {
        compiler.isNegChar = true
        return 1
    }
    if (c == '{') {
        return compiler.setState(::stateCustom, 1)
    }
    if (c == '^') {
        return compiler.setState(::stateCharge, 1)
    }
    if (c== '"') {
        // Комментарий, который становится частью узла
        compiler.pos++
        addNodeItem(compiler, ChemComment(convertComment(scanComment(compiler))))
        return compiler.setState(::stateAgentMid, 1)
    }
    if (c == '$') {
        return compiler.setState(::stateFuncName, 1)
    }
    // Специальный контроль символов нелатинских алфавитов
    if (c in 'А'..'Я' || c in 'а'..'я' || c == 'Ё' || c == 'ё')
        compiler.error("Russian element character", listOf("pos" to pos, "C" to c))
    if (c > 'z')
        compiler.error("Non-latin element character", listOf("pos" to pos, "C" to c));

    return -1
}