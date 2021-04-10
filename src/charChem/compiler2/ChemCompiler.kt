package charChem.compiler2

import charChem.compiler2.chain.ChainSys
import charChem.compiler2.parse.prepareText
import charChem.compiler2.state.stateBegin
import charChem.core.*
import charChem.lang.LangParams

typealias CompilerState = (c: ChemCompiler) -> Int

class ChemCompiler(val srcText: String) {
    val expr = ChemExpr()
    var text = ""
    var pos = 0
    var curState: CompilerState = ::stateBegin
    var curEntity: ChemObj? = null
    var curOp: ChemOp? = null
    var curAgent: ChemAgent? = null
    var curNode: ChemNode? = null
    var curPart: Int = 0
    var elementStartPos: Int = 0
    var preComm: ChemComment? = null
    val chainSys: ChainSys = ChainSys(this)

    fun curChar(): Char = text[pos]
    fun subStr(startPos: Int): String = text.substring(startPos, pos)
    fun isFinish(): Boolean = pos >= text.length

    /**
     * Специальная функция сравнения, соответствует ли указанная строка value
     * содержимому text, начиная с позиции pos
     */
    fun isCurPosEq(value: String): Boolean = value == text.substring(pos, text.length.coerceAtMost(pos + value.length))

    fun error(msgId: String, params: LangParams): Nothing {
        val newParams = params.map {
            val (first, second) = it
            if (first == "pos" && second is Int) {
                "pos" to second + 1
            } else it
        }
        throw ChemError(msgId, newParams)
    }

    fun setState(newState: CompilerState, deltaPos: Int = 0): Int {
        curState = newState
        return deltaPos
    }
}

fun createTestCompiler(text: String): ChemCompiler {
    val compiler = ChemCompiler(text)
    prepareText(compiler)
    return compiler
}