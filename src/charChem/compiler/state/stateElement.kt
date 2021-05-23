package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.addNodeItem
import charChem.compiler.parse.scan
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
    // scanPostItem(compiler) { item.n = it }
    return compiler.setState(::statePostItem)
}