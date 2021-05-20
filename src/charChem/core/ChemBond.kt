package charChem.core

import charChem.math.Point
import charChem.math.is0
import kotlin.math.abs

class ChemBond() : ChemObj() {
    var n: Double = 1.0 // multiplicity of the bond
    var nodes = mutableListOf<ChemNode?>(null, null)
    var index: Int? = null // index of bond in ChemAgent.bonds array
    // TODO: может быть нарушена в closeAgent при удалении дублирующих связей !!!

    var dir: Point? = null  // bond vector
    var tx: String = "" // text description
    var slope: Int = 0 // для связи, созданной из описания / = -1, для \ = 1, для остальных =0
    var isText: Boolean = false
    var color: String? = null // цвет связи
    var w0: Int = 0 // Толщина начала линии, 0 для обычной толщины, 1 для жирной
    var w1: Int = 0 // толщина конца линии
    var isAuto: Boolean = false // Признак связи, пригодной для автокоррекции
    var soft: Boolean = false
    var style: String = "" // Строковый стиль линии. Для двойных и тройных связей каждая линия указывается отдельно
        //  ~ : | I
    var align: Char? = null // Возможные режимы выравнивания двойной связи. x:перекрещенная, m:по центру, l:влево, r:вправо
    var arr0: Boolean = false // Стрелка в обратную сторону
    var arr1: Boolean = false // Стрелка по направлению линии
    var ext: String = "" // Для _o = 'o', для _s = 's'
    var brk: Boolean = false // Устанавливается для конструкции типа -#a-#b-#c-, для связи, предшествующей существующему узлу
    var isNeg: Boolean = false // Использовался символ `
    var isCorr: Boolean = false // Выполнена коррекция наклона с 30 до 60 градусов
    var isCycle: Boolean = false // Циклическая связь. Всегда true для _o и может быть для _s
    var middlePoints: List<Point>? = null // Дополнительные точки для искривленных связей.
    // Если связь имеет дополнительные точки, то она не будет мержиться с другими связями между этими же узлами

    // Position calculate for second part of bond
    fun calcPt(): Point = nodes[0]!!.pt + dir!!

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

    fun setHydrogen() {
        style = ":"
        n = 0.0
    }
    fun setCross() {
        style = "x"
    }
    fun isCross() = style == "x"

    fun checkText() {
        // Связь считается текстовой, если она расположена горизонтально и имеет длину =1
        isText = dir?.let { is0(abs(it.x) -1.0) && is0(it.y) } ?: false
    }

    fun linearText(): String = if (isAuto) {
        var res = tx
        if (isNeg) res = "`$res"
        if (is0(n)) res += '0'
        res
    } else {
        tx
    }

    fun isHorizontal(): Boolean = dir?.let { !is0(it.x) && is0(it.y) } ?: false
}