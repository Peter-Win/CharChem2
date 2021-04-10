package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.addNodeItem
import charChem.compiler2.parse.scanTo
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
    addNodeItem(compiler, ChemRadical.dict[s] ?: ChemCustom(s))
    return compiler.setState(::statePostItem, 1)
}