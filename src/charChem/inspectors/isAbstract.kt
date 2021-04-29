package charChem.inspectors

import charChem.core.*

/**
 * Является ли указанное выражение абстрактным.
 * Это происходит при следующих условиях:
 * - Наличие абстрактного элемента: {R}-OH
 * - Наличие нечисловых коэффициентов: C'n'H'2n+2'
 * - Запятая: (Ca,Mg)SO4
 */
fun isAbstract(chemObj: ChemObj): Boolean {
    val visitor = IsAbstractVisitor()
    chemObj.walk(visitor)
    return visitor.isStop
}

fun isAbsK(k: ChemK?): Boolean = k?.let{ !it.isNumber() } ?: false

class IsAbstractVisitor() : Visitor() {
    override fun agentPre(obj: ChemAgent) {
        isStop = isAbsK(obj.n)
    }

    override fun itemPre(obj: ChemNodeItem) {
        isStop = isAbsK(obj.n)
    }

    override fun bracketEnd(obj: ChemBracketEnd) {
        isStop = isAbsK(obj.n)
    }

    override fun mul(obj: ChemMul) {
        isStop = isAbsK(obj.n)
    }

    override fun custom(obj: ChemCustom) {
        isStop = true
    }

    override fun comma(obj: ChemComma) {
        isStop = true
    }
}