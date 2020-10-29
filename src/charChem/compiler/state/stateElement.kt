package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.addNodeItem
import charChem.core.ChemRadical
import charChem.core.ChemSubObj
import charChem.core.findElement

fun stateElement(compiler: ChemCompiler): Int {
    while (!compiler.isFinish() && compiler.curChar() in 'a'..'z') compiler.pos++
    val elemId = compiler.subStr(compiler.elemStartPos)
    val elem: ChemSubObj = findElement(elemId)
            ?: ChemRadical.dict[elemId]
            ?: compiler.error(
                    "Unknown element '[Elem]'",
                    listOf("pos" to compiler.elemStartPos, "Elem" to elemId)
            )
    addNodeItem(compiler, elem)
    return compiler.setState(::statePostItem)
}