package charChem.core

import charChem.utils.romanNum

class ChemCharge(
        val text: String = "", // Text description, for example: '2+'
        val value: Double = 0.0, // number value, for example: 2
        var isLeft: Boolean = false, // ⁺N
        val isRound: Boolean = false, // A sign of drawing a charge inside a circle
)

val leftSigned = Regex("""(^|(^[-+]))\d+$""")
val rightSigned = Regex("""^\d+[-+]$""")

/**
 * Create charge object from text description
 * Особенностью любого текстового описания заряда в том, что при компиляции происходят попытки,
 * начиная с одного символа и увеличивая количество символов до тех пор, пока это валидное выражение.
 * То есть, не может быть такого, что двухсимвольное описание валидно, а односимвольное - нет.
 */
fun createCharge(chargeDescr: String, isLeft: Boolean = false): ChemCharge? {
    if (chargeDescr == "") return null
    val text = chargeDescr.replace('–', '-') // Replace similar characters
    if (text in setOf("-", "--", "---"))
    // One or more minuses:	O^--
        return ChemCharge(text, -text.length.toDouble(), isLeft)
    // One or more pluses: Zn^++
    if (text in setOf("+", "++", "+++"))
        return ChemCharge(text, text.length.toDouble(), isLeft)
    // A number with a plus or minus front: S^+6, O^-2
    if (leftSigned.matches(text))
        return ChemCharge(text, text.toDouble(), isLeft)
    // A number with plus or minus behind: Ca^2+, PO4^3-
    if (rightSigned.matches(text))
        return ChemCharge(text, "${text.last()}${text.substring(0, text.length - 1)}".toDouble(), isLeft)
    if (text == "+o")
        return ChemCharge("+", 1.0, isLeft, true)
    if (text == "-o")
        return ChemCharge("-", -1.0, isLeft, true)
    val v = romanNum.getOrDefault(text, 0)
    if (v != 0)
        return ChemCharge(text.toUpperCase(), v.toDouble(), isLeft)
    return null
}
