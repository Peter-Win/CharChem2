package charChem.inspectors

import charChem.compiler.compile
import charChem.core.*
import charChem.textRules.RulesBase
import charChem.textRules.rulesText

data class CtxItem(var text: String = "", var neg: Boolean = false)

fun locateAtomNumber(item: ChemObj): Int {
    var number = 0
    item.walk(object: Visitor() {
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
    val stack = mutableListOf(CtxItem())
    var first = true
    fun ctxOut(text: String) {
        stack[0].text += text
    }
    fun space() {
        if (first) {
            first = false
        } else {
            ctxOut(" ")
        }
    }
    fun setNeg() {
        stack[0].neg = true
    }
    fun drawCharge(charge: ChemCharge?, isPrefix: Boolean) = charge?.let {
        if (isPrefix == it.isLeft) {
            ctxOut(rules.nodeCharge(it))
        }
    }
    var autoNode = false

    obj.walk(object: Visitor() {
        override fun agentPre(obj: ChemAgent) {
            space()
            if (obj.n.isSpecified()) {
                ctxOut(rules.agentK(obj.n))
            }
        }

        override fun atom(obj: ChemAtom) {
            if (!autoNode) {
                ctxOut(rules.atom(obj.id))
            }
        }

        override fun comma(obj: ChemComma) {
            ctxOut(rules.comma())
        }

        override fun comment(obj: ChemComment) {
            ctxOut(rules.comment(obj.text))
        }

        override fun custom(obj: ChemCustom) {
            ctxOut(rules.custom(obj.text))
        }

        override fun itemPre(obj: ChemNodeItem) {
            if (autoNode) return
            val rawAtomNum = obj.atomNum
            if (rawAtomNum != null) {
                // Вывести двухэтажную конструкцию: масса/атомный номер слева от элемента
                val atomNum = if (rawAtomNum >= 0) rawAtomNum else locateAtomNumber(obj)
                ctxOut(rules.itemMassAndNum(obj.mass, atomNum))
            } else if (obj.mass != 0.0) {
                ctxOut(rules.itemMass(obj.mass))
            }
        }
        override fun itemPost(obj: ChemNodeItem) {
            if (autoNode) return
            if (obj.n.isSpecified())
                ctxOut(rules.itemCount(obj.n))
        }

        override fun nodePre(obj: ChemNode) {
            drawCharge(obj.charge, true)
            if (obj.autoMode) {
                autoNode = true
            }
        }
        override fun nodePost(obj: ChemNode) {
            drawCharge(obj.charge, false)
            autoNode = false
        }

        override fun operation(obj: ChemOp) {
            space()
            ctxOut(rules.operation(obj))
        }

        override fun radical(obj: ChemRadical) {
            ctxOut(rules.radical(obj.label))
        }

        override fun bond(obj: ChemBond) {
            ctxOut("${if (obj.isNeg) "`" else ""}${obj.tx}")
        }

        override fun bracketBegin(obj: ChemBracketBegin) {
            drawCharge(obj.end?.charge, true)
            ctxOut(obj.text)
        }

        override fun bracketEnd(obj: ChemBracketEnd) {
            ctxOut(obj.text)
            if (obj.n.isSpecified())
                ctxOut(rules.itemCount(obj.n))
            drawCharge(obj.charge, false)
        }

        override fun mul(obj: ChemMul) {
            if (!obj.isFirst) ctxOut(rules.mul())
            ctxOut(rules.mulK(obj.n))
        }
    })
    return stack[0].text
}

fun makeTextFormula(sourceText: String, rules: RulesBase): String {
    val expr = compile(sourceText)
    if (!expr.isOk())
        return ""
    return makeTextFormula(expr, rules)
}
