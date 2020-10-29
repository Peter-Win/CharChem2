package charChem.inspectors

import charChem.core.ChemAgent
import charChem.core.ChemNode
import charChem.core.ChemObj
import charChem.core.Visitor

/**
 * Вычисление заряда объекта.
 * Используется для объектов от узла и выше.
 * При наличии абстрактных коэффициентов возвращается NaN
 */
fun calcCharge(obj: ChemObj): Double {
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
        override fun agentPost(obj: ChemAgent) = pop { obj.n.num }

        // TODO: Пока нет поддержки скобок и множителей

        override fun nodePost(obj: ChemNode) {
            // Ниже уровня узла заряд не проверяем
            obj.charge?.let { stack[0] += it.value }
        }
    })
    return stack[0]
}