package charChem.compiler

import charChem.compiler.chain.ChainSys
import charChem.compiler.main.*
import charChem.compiler.parse.prepareText
import charChem.compiler.state.stateBegin
import charChem.core.*
import charChem.lang.LangParams
import charChem.math.Point

typealias CompilerState = (c: ChemCompiler) -> Int

data class MiddlePoint(val pt: Point, val pos: Int)

class ChemCompiler(val srcText: String) {
    val expr = ChemExpr()
    var text = ""
    var pos = 0
    var curState: CompilerState = ::stateBegin
    var curEntity: ChemObj? = null
    var curOp: ChemOp? = null
    var curAgent: ChemAgent? = null
    var curNode: ChemNode? = null
    var curBond: ChemBond? = null
    var chargeOwner: ChemChargeOwner? = null // Объект, к которому применится конструкция ^
    var curPart: Int = 0
    var elementStartPos: Int = 0
    var preComm: ChemComment? = null
    val chainSys = ChainSys(this)
    val references = mutableMapOf<String, ChemNode>()
    var mulCounter: MulCounter = MulCounter()
    val varsDict = mutableMapOf<String, Double>()
    var curWidth = 0
    val nodesBranch = NodesBranch()
    val middlePoints = mutableListOf<MiddlePoint>()

    private val stack = mutableListOf<StackItem>()
    fun push(item: StackItem) = stack.add(0, item)
    fun pop(): StackItem? = if (stack.size == 0) null else stack.removeAt(0)

    private var _altFlag: Boolean = false
    fun setAltFlag() {
        _altFlag = true
    }
    fun getAltFlag(): Boolean {
        val value = _altFlag
        _altFlag = false
        return value
    }

    var varSlope: Double = 0.0
    var varLength: Double = 1.0
    var varMass: Double = 0.0 // special mass for next element - $M()
    var varAtomNumber: Int? = null // number in $nM(mass, number)
    var varColor: String? = null
    var varItemColor: String? = null
    var varItemColor1: String? = null
    var varAtomColor: String? = null
    var varAtomColor1: String? = null
    var varAlign: Char? = null

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