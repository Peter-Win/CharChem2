package charChem.core

import kotlin.math.abs
import kotlin.math.round

// Chemical Coefficient.
// Can be number or string (abstract coefficient: C'n'H'2n+2')

class ChemK private constructor(val num: Double, val text: String) {
    constructor(n: Int) : this(n.toDouble(), "")
    constructor(n: Double) : this(n, "")
    constructor(text: String) : this(Double.NaN, text)
    // коэффициент задан явным образом (не равен 1)
    fun isSpecified() = text.isNotEmpty() || num != 1.0
    fun isNumber() = text.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (other is Number && isNumber())
            return num == other.toDouble()
        if (other is String && !isNumber())
            return text == other
        if (other !is ChemK)
            return false
        if (text.isNotEmpty())
            return text == other.text
        return num == other.num
    }
    override fun toString(): String {
        if (!isNumber())
            return text
        val iNum: Double = round(num)
        if (abs(num - iNum) < 0.05)
            return iNum.toInt().toString()
        return num.toString()
    }
}