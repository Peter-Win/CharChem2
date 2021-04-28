package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.addNodeItem
import charChem.compiler.main.getLastItem
import charChem.compiler.parse.scanPostItem
import charChem.compiler.parse.scanTo
import charChem.core.ChemCustom
import charChem.core.ChemRadical

/**
 * Создание абстрактного элемента или радикала
 */
fun stateCustom(compiler: ChemCompiler): Int {
    val startPos = compiler.pos // pos установлен на символ, следующий за '{'
    if (!scanTo(compiler, '}'))
        compiler.error("Abstract element is not closed", listOf("pos" to startPos-1))
    val s = compiler.subStr(startPos)
    val item = addNodeItem(compiler, ChemRadical.dict[s] ?: ChemCustom(s))
    compiler.pos++
    scanPostItem(compiler) { item.n = it }
    return compiler.setState(::stateAgentMid)
}