package charChem.compiler

import charChem.compiler.chains.ChainBox
import charChem.compiler.state.*
import charChem.core.*
import charChem.lang.LangParams
import charChem.math.Point
import charChem.inspectors.makeTextFormula

// Usage example:
// val expr = ChemCompiler("H2O").exec()

typealias CompilerState = (c: ChemCompiler) -> Int

public class ChemCompiler(private val sourceText: String) {
    private val expr: ChemExpr = ChemExpr()
    var pos: Int = 0
    var text: String = ""
    var curState: CompilerState = ::stateBegin
    var curEntity: ChemObj? = null
    var curAgent: ChemAgent? = null
    var curNode: ChemNode? = null
    var curBond: ChemBond? = null
    var prevBond: ChemBond? = null
    var prefixK: ChemK? = null
    var curPart: Int = 0    // Номер части в хим выражении. Части разделяются операциями с признаком eq, н.р. = или ->
    var elemStartPos: Int = 0
    var chargeOwner: ChemChargeOwner? = null // Объект, к которому применится конструкция ^
    var isNegChar: Boolean = false
    var commentPre: ChemComment? = null
    var specMass: Double = 0.0 // special mass for next element - $M()
    var customAtomNumber: Int? = null // number in $nM(mass, number)
    var chainBox: ChainBox = ChainBox()
    var userSlope: Double = 0.0  // result of $slope(). Not used, if 0

    fun resetVariables() {
        userSlope = 0.0
    }

    fun exec(): ChemExpr {
        try {
            prepareText()
            // Основной цикл синтаксического анализа
            while (!isFinish()) {
                val step = curState(this)
                pos += step
            }
            closeEntity()
        } catch (e: ChemError) {
            expr.error = e
        }
        return expr
    }

    fun isFinish(): Boolean = pos >= text.length

    fun prepareText() {
        // В различных источниках часто встречается символ, похожий на минус, но с другим кодом...
        text = sourceText.replace("−", "-");
        expr.src0 = text
        // Выполнить препроцесс
        // text = result.src = _preProcess(text);
        expr.src = text
        // Добавить пробел в конец описания для упрощения алгоритма распознавания
        text += ' ';
    }

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

    fun skipSpace(): Char {
        while (pos < text.length && isSpace(text[pos])) pos++
        return if (pos == text.length) ' ' else text[pos]
    }

    fun curChar(): Char = text[pos]
    fun subStr(startPos: Int): String = text.substring(startPos, pos)
    fun scanTo(ch: Char): Boolean {
        while (!isFinish() && curChar() != ch) pos++
        return !isFinish()
    }

    fun parseNum(value: String, valuePos: Int): Double {
        return value.toDoubleOrNull()
                ?: error(
                        "Invalid number [n]",
                        listOf<Pair<String, Any>>("n" to value, "pos" to valuePos)
                )
    }

    fun createEntity(entity: ChemObj) {
        closeEntity()
        curEntity = entity
        expr.entities.add(entity)
        entity.walk(object : Visitor() {
            override fun agentPre(obj: ChemAgent) = openAgent(obj)
        })
    }

    fun closeEntity() {
        val entity = curEntity
        if (entity != null) {
            curEntity = null
            entity.walk(object : Visitor() {
                override fun agentPre(obj: ChemAgent) = closeAgent()
            })
        }
    }

    fun openAgent(agent: ChemAgent) {
        curAgent = agent
        agent.part = curPart
        commentPre?.let {
            // Если есть комментарий, создать узел и включить в него.
            getForcedNode().items.add(ChemNodeItem(it))
            commentPre = null
        }
    }

    fun closeAgent() {
        curAgent?.let { agent ->
            closeChain()
            chainBox.clear()
            prefixK?.let {
                agent.n = it
                prefixK = null
            }
            resolveAutoNodes(agent)
            curAgent = null
            resetVariables()
        }
    }

    fun getForcedNode(isAuto: Boolean = false): ChemNode {
        return curNode ?: openNode(isAuto)
    }

    fun openNode(isAuto: Boolean = false): ChemNode {
        closeNode()
        val node = curAgent!!.addNode(ChemNode())
        node.autoMode = isAuto
        curNode = node
        chargeOwner = node
        return node
    }

    fun closeNode() {
        curNode?.let {
            closeItem()
            chargeOwner = null
            curNode = null
        }
    }

    fun closeBond() {
        curBond?.let { bond ->
            prevBond = bond
            if (bond.nodes[1] == null) {
                bond.nodes[1] = getForcedNode(true)
            }
            // Если оба узла автоматические, то мягкая связь становится жесткой
            if (bond.soft) {
                val node0 = bond.nodes[0]
                val node1 = bond.nodes[1]
                if (node0 != null && node0.autoMode && node1 != null && node1.autoMode) {
                    bond.soft = false
                }
            }
            if (!bond.soft) {
                // Если не мягкая связь, то вычислить координаты второго узла
                bond.nodes[1]?.let { node1 ->
                    bond.nodes[0].let { node0 ->
                        node1.pt = bond.calcPt()
                    }
                }
            }
            curBond = null
        }
    }

    fun closeChain() {
        closeBond()
        closeNode()
        prevBond = null
    }

    fun closeItem() {
        getLastItem()?.let {
            if (it.atomNum == -1) {
                it.walk(object : Visitor() {
                    override fun atom(obj: ChemAtom) {
                        it.atomNum = obj.n
                    }
                })
            }
            if (it.atomNum == -1) {
                it.atomNum = null
            }
        }
    }

    fun getLastItem(): ChemNodeItem? = curNode?.items?.lastOrNull()
}
