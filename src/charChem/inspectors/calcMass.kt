package charChem.inspectors

import charChem.core.*

/**
 * Вычисление массы указанного химического объекта.
 * @param obj Любой химический объект (выражение, агент, узел и т.п.)
 * @param applyAgentK Если указать false, то игнорируются коэффициенты агентов.
 *
 * Примечание 1. Рекомендуется использовать функцию isAbstract прежде чем вызывать calcMass.
 * Т.к. если в состав объекта входят абстрактные элементы, то результат непредсказуем.
 * Никакого исключения при этом не генерируется.
 * Например, для выражения {R}-OH мы получим сумму масс O и H
 *
 * Примечание 2. Эта функция не слишком полезна для выражений с несколькими агентами.
 * Поэтому для выражений ChemExpr рекомендуется использовать метод mass.
 */
fun calcMass(obj: ChemObj, applyAgentK: Boolean = true): Double {
    val stack = mutableListOf<Double>(0.0)
    fun push() {
        stack.add(0, 0.0)
    }
    fun pop(calc: () -> Double) {
        val value: Double = stack[0] * calc()
        stack.removeAt(0)
        stack[0] += value
    }

    obj.walk(object : Visitor() {
        override fun agentPre(obj: ChemAgent) = push()

        override fun agentPost(obj: ChemAgent) =
                pop { if (applyAgentK) obj.n.num else 1.0 }

        override fun mul(obj: ChemMul) = push()
        override fun mulEnd(obj: ChemMulEnd) = pop { obj.begin.n.num }

        override fun bracketBegin(obj: ChemBracketBegin) = push()
        override fun bracketEnd(obj: ChemBracketEnd) = pop { obj.n.num }

        override fun nodePre(obj: ChemNode) = push()
        override fun nodePost(obj: ChemNode) = pop { 1.0 }

        override fun itemPre(obj: ChemNodeItem) = push()
        override fun itemPost(obj: ChemNodeItem) {
            if (obj.mass != 0.0) {
                // явно указанная масса $M() более приоритетна, чем вычисленная
                stack[0] = obj.mass
            }
            pop { obj.n.num }
        }

        override fun atom(obj: ChemAtom) {
            stack[0] += obj.mass
        }

        override fun radical(obj: ChemRadical) {
            obj.items.list.forEach { listItem ->
                stack[0] += listItem.n * (listItem.elem?.mass ?: 0.0)
            }
        }
    })
    return stack[0]
}