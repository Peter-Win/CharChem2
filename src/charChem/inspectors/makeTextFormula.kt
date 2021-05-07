package charChem.inspectors

import charChem.compiler.compile
import charChem.core.*
import charChem.textRules.RulesBase
import charChem.textRules.rulesText

private data class Chunk(val text: String, val color: String?)

private class StackItem {
    var l2r: Boolean = true
    val chunks = mutableListOf<Chunk>()
    fun add(chunk: Chunk) {
        if (l2r) {
            chunks.add(chunk)
        } else {
            chunks.add(0, chunk)
        }
    }
}

private fun locateAtomNumber(item: ChemObj): Int {
    var number = 0
    item.walk(object : Visitor() {
        override fun atom(obj: ChemAtom) {
            number = obj.n
        }
    })
    return number
}

/**
 * Сформировать текстовое представление химической формулы.
 * Не все формулы могут быть представлены в виде текста.
 * Поэтому перед вызовом этой функции нужно использовать isTextFormula
 */
fun makeTextFormula(obj: ChemObj, rules: RulesBase = rulesText): String {
    val stack = mutableListOf(StackItem())
    var itemColor: String? = null
    var atomColor: String? = null

    fun push() {
        stack.add(0, StackItem())
    }

    fun pop() {
        if (stack.size == 1) {
            return
        }
        val item = stack.removeAt(0)
        if (stack[0].l2r) {
            stack[0].chunks.addAll(item.chunks)
        } else {
            stack[0].chunks.addAll(0, item.chunks)
        }
    }

    fun ctxOut(text: String, color: String?) {
        stack[0].add(Chunk(text, color))
    }

    fun space() {
        ctxOut(" ", null)
    }

    fun drawCharge(charge: ChemCharge?, isPrefix: Boolean, color: String?) = charge?.let {
        if (isPrefix == it.isLeft) {
            ctxOut(rules.nodeCharge(it), color)
        }
    }

    var autoNode = false

    obj.walk(object : Visitor() {
        override fun agentPre(obj: ChemAgent) {
            space()
            push()
            if (obj.n.isSpecified()) {
                ctxOut(rules.agentK(obj.n), null)
            }
        }

        override fun agentPost(obj: ChemAgent) = pop()

        override fun atom(obj: ChemAtom) {
            if (!autoNode) {
                ctxOut(rules.atom(obj.id), atomColor ?: itemColor)
            }
        }

        override fun bond(obj: ChemBond) {
            stack[0].l2r = !obj.isNeg
            ctxOut(obj.tx, obj.color)
        }

        override fun comma(obj: ChemComma) {
            ctxOut(rules.comma(), itemColor)
        }

        override fun comment(obj: ChemComment) {
            ctxOut(rules.comment(obj.text), itemColor)
        }

        override fun custom(obj: ChemCustom) {
            ctxOut(rules.custom(obj.text), itemColor)
        }

        override fun itemPre(obj: ChemNodeItem) {
            if (autoNode) return
            itemColor = obj.color
            atomColor = obj.atomColor
            val rawAtomNum = obj.atomNum
            if (rawAtomNum != null) {
                // Вывести двухэтажную конструкцию: масса/атомный номер слева от элемента
                val atomNum = if (rawAtomNum >= 0) rawAtomNum else locateAtomNumber(obj)
                ctxOut(rules.itemMassAndNum(obj.mass, atomNum), itemColor)
            } else if (obj.mass != 0.0) {
                ctxOut(rules.itemMass(obj.mass), itemColor)
            }
        }

        override fun itemPost(obj: ChemNodeItem) {
            if (autoNode) return
            if (obj.n.isSpecified())
                ctxOut(rules.itemCount(obj.n), itemColor)
        }

        override fun nodePre(obj: ChemNode) {
            push()
            drawCharge(obj.charge, true, itemColor)
            if (obj.autoMode) {
                autoNode = true
            }
        }

        override fun nodePost(obj: ChemNode) {
            drawCharge(obj.charge, false, itemColor)
            autoNode = false
            pop()
        }

        override fun operation(obj: ChemOp) {
            space()
            ctxOut(rules.operation(obj), obj.color)
        }

        override fun radical(obj: ChemRadical) {
            ctxOut(rules.radical(obj.label), itemColor)
        }

        override fun bracketBegin(obj: ChemBracketBegin) {
            push()
            drawCharge(obj.end?.charge, true, obj.color)
            ctxOut(obj.text, obj.color)
        }

        override fun bracketEnd(obj: ChemBracketEnd) {
            val color = obj.begin.color
            ctxOut(obj.text, color)
            if (obj.n.isSpecified())
                ctxOut(rules.itemCount(obj.n), color)
            drawCharge(obj.charge, false, color)
            pop()
        }

        override fun mul(obj: ChemMul) {
            if (!obj.isFirst) ctxOut(rules.mul(), obj.color)
            ctxOut(rules.mulK(obj.n), obj.color)
        }
    })
    val nonOptimized = buildTextFromChunks(stack[0].chunks, rules).trim()
    return rules.postProcess(nonOptimized)
}

private fun buildTextFromChunks(chunks: List<Chunk>, rules: RulesBase): String {
    val tags = chunks.mapIndexed { index, chunkItem ->
        var result = chunkItem.text
        chunkItem.color?.let { color ->
            val needOpen = index == 0 || color != chunks[index - 1].color
            val needClose = index == chunks.size - 1 || color != chunks[index + 1].color
            if (needOpen) result = rules.colorBegin(color) + result
            if (needClose) result += rules.colorEnd()
        }
        result
    }
    return tags.fold("") { acc: String, tag ->
        acc + tag
    }
}

fun makeTextFormula(sourceText: String, rules: RulesBase): String {
    val expr = compile(sourceText)
    if (!expr.isOk())
        return ""
    return makeTextFormula(expr, rules)
}
