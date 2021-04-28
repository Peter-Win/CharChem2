package charChem.core

import charChem.inspectors.calcMass
import charChem.lang.Lang

class ChemExpr() : ChemObj() {
    var error: ChemError? = null
    // Source description
    var src0: String = ""
    // Description after preprocessing
    var src: String = ""
    // Entities: reagents and operations
    var entities: MutableList<ChemObj> = mutableListOf()

    // Check for success. If false, then an error.
    fun isOk(): Boolean = error == null
    // Extended error message. Empty string, if not error
    fun getMessage(locale: String? = null): String = error?.let { err ->
        locale?.let {
            val oldLang = Lang.curLang
            Lang.curLang = it
            val msg = err.message
            Lang.curLang = oldLang
            msg
        } ?: err.message
    } ?: ""

    override fun walk(visitor: Visitor) {
        for (entity in entities) {
            visitor.entityPre(entity)
            if (visitor.isStop)
                return
            entity.walk(visitor)
            if (visitor.isStop)
                return
            visitor.entityPost(entity)
            if (visitor.isStop)
                return
        }
    }

    fun getAgents(): List<ChemAgent> {
        // Правильно было бы использовать walk.
        // Но этот вариант работает быстрее, т.к. walk обходит все подчиненные объекты.
        // А здесь просто цикл по сущностям верхего уровня, которых обычно не более 10.
        val result = mutableListOf<ChemAgent>()
        entities.forEach{ if (it is ChemAgent) result.add(it) }
        return result
    }

    /**
     * Если выражение состоит более чем из одного агента (а это не редкость),
     * то считать его общую массу через calcMass не имеет смысла.
     * Данная функция считает массу каждого агента отдельно.
     * @param applyK Если false, то не учитываются коэффициенты перед агентами.
     */
    fun mass(applyK: Boolean = true): List<Double> =
        getAgents().map { calcMass(it, applyK) }
}