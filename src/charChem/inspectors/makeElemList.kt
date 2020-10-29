package charChem.inspectors

import charChem.core.*

/**
 * Формирование списка элементов из выражения.
 * Не учитываются коэффициенты агентов.
 * Не имеет смысла для выражений, имеющих более одного агента.
 */
fun makeElemList(obj: ChemObj): ElemList {
    val stack = mutableListOf(ElemList())
    fun push() = stack.add(0, ElemList())
    fun pop(k: Double) {
        val list = stack[0]
        stack.removeAt(0)
        list.scale(k)
        stack[0].addList(list)
    }

    obj.walk(object: Visitor() {
        override fun agentPre(obj: ChemAgent) = push()
        override fun agentPost(obj: ChemAgent) = pop(obj.n.num)

        override fun nodePost(obj: ChemNode) {
            stack[0].charge += obj.charge?.value ?: 0.0
        }

        override fun itemPre(obj: ChemNodeItem) = push()
        override fun itemPost(obj: ChemNodeItem) = pop(obj.n.num)
        override fun atom(obj: ChemAtom) {
            stack[0].addElem(obj)
        }
        override fun custom(obj: ChemCustom) {
            stack[0].addCustom(obj.text)
        }
        override fun radical(obj: ChemRadical) {
            stack[0].addRadical(obj)
        }
    })
    return stack[0]
}