package charChem.compiler

class BondDef(
        val descr: String,
        val multiplicity: Int,
        degrees: Double,
        val slopeSign: Int, // знак наклона
        val isSoft: Boolean = false,
        val text: String? = null,
) {
    val angle = degrees * Math.PI / 180.0
    fun isHoriz() = angle == 0.0 && slopeSign == 0
}

// Список построен так, что при проходе от начала до конца будет найдена последовательность, самая длинная из возможных
val shortBondsList = listOf<BondDef>(
        BondDef("-", 1, 0.0, 0, true),
        BondDef("\u2013", 1, 0.0, 0, true),
        BondDef("=", 2, 0.0, 0, true),
        BondDef("%", 3, 0.0, 0, true, "≡"),
        BondDef("≡", 3, 0.0, 0, true, "≡"),
        BondDef("--", 1, 0.0, 0, false, "-"),
        BondDef("==", 2, 0.0, 0, false, "="),
        BondDef("%%", 3, 0.0, 0, false, "≡"),
        BondDef("|", 1, 90.0, 0),
        BondDef("||", 2, 90.0, 0),
        BondDef("|||", 3, 90.0, 0),
        BondDef("/", 1, 0.0, -1),
        BondDef("//", 2, 0.0, -1),
        BondDef("///", 3, 0.0, -1),
        BondDef("\\", 1, 0.0, 1),
        BondDef("\\\\", 2, 0.0, 1),
        BondDef("\\\\\\", 3, 0.0, 1),
)

object BondDefs {
    val starts: Set<Char> by lazy {
        HashSet(shortBondsList.map { it.descr[0] })
    }
}

// Такая функция позволяет выполнить сравнение подстроки без создания дополнительных объектов
fun isSubEqual(value: String, text: String, pos: Int): Boolean {
    var k = 0
    var j = pos
    while (k < value.length && j < text.length) {
        if (value[k] != text[j]) {
            return false
        }
        k++
        j++
    }
    return k == value.length
}

fun findBondDef(text: String, pos: Int): BondDef? {
    var result: BondDef? = null
    shortBondsList.forEach {
        if (isSubEqual(it.descr, text, pos)) {
            result = it
        }
    }
    return result
}