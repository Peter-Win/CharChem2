package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.addNodeItem
import charChem.core.ChemCustom
import charChem.core.ChemNodeItem
import charChem.core.ChemRadical

/**
 * Создание абстрактного элемента или радикала
 */
fun stateCustom(compiler: ChemCompiler): Int {
    val startPos = compiler.pos // pos установлен на символ, следующий за '{'
    if (!compiler.scanTo('}'))
        compiler.error("Abstract element is not closed", listOf("pos" to startPos-1))
    val s = compiler.subStr(startPos)
    addNodeItem(compiler, ChemRadical.dict[s] ?: ChemCustom(s))
    return compiler.setState(::statePostItem, 1)
}