package charChem.core

import charChem.math.Point

class ChemBond() : ChemObj() {
    var n: Double = 1.0 // multiplicity of the bond
    var nodes: Array<ChemNode?> = arrayOf(null, null)
    var index: Int? = null // index of bond in ChemAgent.bonds array
    // TODO: может быть нарушена в closeAgent при удалении дублирующих связей !!!

    var pt: Point? = null  // bond vector
    var tx: String = "" // text description
    var slope: Double = 0.0 // для связи, созданной из описания / = -1, для \ = 1, для остальных =0
    var isText: Boolean = false
    var color: String = "" // цвет связи
    var w0: Double = 0.0 // Толщина начала линии, 0 для обычной толщины, 1 для жирной
    var w1: Double = 0.0 // толщина конца линии
    var isAuto: Boolean = false // Признак связи, пригодной для автокоррекции
    var soft: Boolean = false
    var style: String = "" // Строковый стиль линии. Для двойных и тройных связей каждая линия указывается отдельно
    var align: String = "" // Возможные режимы выравнивания двойной связи. x:перекрещенная, m:по центру, l:влево, r:вправо
    var arr0: Boolean = false // Стрелка в обратную сторону
    var arr1: Boolean = false // Стрелка по направлению линии
    var ext: String = "" // Для _o = 'o', для _s = 's'
    var brk: Boolean = false // Устанавливается для конструкции типа -#a-#b-#c-, для связи, предшествующей существующему узлу

    // Position calculate for second part of bond
    fun calcPt() = nodes[0]!!.pt + pt!!

    // Get another node of bond
    fun other(node: ChemNode): ChemNode? {
        if (nodes.size != 2) return null
        if (nodes[0] === node) return nodes[1]
        if (nodes[1] === node) return nodes[0]
        return null
    }

    override fun walk(visitor: Visitor) {
        visitor.bond(this)
    }
}