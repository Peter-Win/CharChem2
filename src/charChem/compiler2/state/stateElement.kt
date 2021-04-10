package charChem.compiler2.state

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.addNodeItem
import charChem.compiler2.parse.scan
import charChem.core.ChemRadical
import charChem.core.ChemSubObj
import charChem.core.findElement

// Извлечение элемента. Позиция первого символа elementStartPos
fun stateElement(compiler: ChemCompiler): Int {
    scan(compiler) { it in 'a'..'z' }
    val id = compiler.subStr(compiler.elementStartPos)
    val elem: ChemSubObj = findElement(id) ?: ChemRadical.dict[id] ?: compiler.error(
            "Unknown element '[Elem]'",
            listOf("pos" to compiler.elementStartPos, "Elem" to id)
    )
    addNodeItem(compiler, elem)
    return compiler.setState(::statePostItem)
}