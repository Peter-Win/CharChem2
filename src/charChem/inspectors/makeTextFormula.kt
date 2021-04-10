package charChem.inspectors

import charChem.compiler2.compile
import charChem.core.*
import charChem.textRules.RulesBase
import charChem.textRules.rulesText

data class CtxItem(var text: String = "", var neg: Boolean = false)

/**
 * Сформировать текстовое представление химической формулы.
 * Не все формулы могут быть представлены в виде текста.
 * Поэтому перед вызовом этой функции нужно использовать isTextFormula
 */
fun makeTextFormula(obj: ChemObj, rules: RulesBase = rulesText): String {
    val stack = mutableListOf(CtxItem())
    var first: Boolean = true
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
    obj.walk(object: Visitor() {
        override fun agentPre(obj: ChemAgent) {
            space()
            if (obj.n.isSpecified()) {
                ctxOut(rules.agentK(obj.n))
            }
        }

        override fun atom(obj: ChemAtom) {
            ctxOut(rules.atom(obj.id))
        }

        override fun comment(obj: ChemComment) {
            ctxOut(rules.comment(obj.text))
        }

        override fun custom(obj: ChemCustom) {
            ctxOut(rules.custom(obj.text))
        }

        override fun itemPre(obj: ChemNodeItem) {
            if (obj.atomNum != null) {
                // Вывести двухэтажную конструкцию: масса/атомный номер слева от элемента
                ctxOut(rules.itemMassAndNum(obj.mass, obj.atomNum!!))
            } else if (obj.mass != 0.0) {
                ctxOut(rules.itemMass(obj.mass))
            }
        }
        override fun itemPost(obj: ChemNodeItem) {
            if (obj.n.isSpecified())
                ctxOut(rules.itemCount(obj.n))
        }

        override fun nodePre(obj: ChemNode) {
            drawCharge(obj.charge, true)
        }
        override fun nodePost(obj: ChemNode) {
            drawCharge(obj.charge, false)
        }

        override fun operation(obj: ChemOp) {
            space()
            ctxOut(rules.operation(obj))
        }

        override fun radical(obj: ChemRadical) {
            ctxOut(rules.radical(obj.label))
        }

        override fun bond(obj: ChemBond) {
            ctxOut(obj.tx)
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
