package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parseNode

/**
 * Начало агента.
 * Если в нем не удалось распознать узел, значит это ошибка.
 */
fun stateAgentIn(compiler: ChemCompiler): Int {
    val res: Int = parseNode(compiler)
    if (res < 0) {
        compiler.error(
                "Unknown element character '[C]'",
                listOf("C" to compiler.curChar(), "pos" to compiler.pos)
        )
    }
    return res;
}