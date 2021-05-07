package charChem.textRules

import charChem.core.ChemCharge
import charChem.core.ChemK
import charChem.core.ChemOp
import charChem.math.strMass

/*
Эти правила дают не очень качественный результат.
Пока нет поддержки цветов. И некрасиво выводятся операции с подписями
 */

val specialCharacters = setOf<Char>('\'', '*', ':', '\\', '_', '{', '|', '}', '~')

fun rtfChar(src: Char): String {
    if (src in specialCharacters) return "\\$src"
    if (src.toInt() > 127) return "{\\cf2\\rtlch \\ltrch\\loch \\u${src.toInt()}\\'3f}"
    return "$src"
}

fun escapeRTF(text: String): String = text.fold("") { src, c -> src + rtfChar(c)}

fun subRTF(text: String): String = "{\\sub ${escapeRTF(text)}}"
fun supRTF(text: String): String = "{\\super ${escapeRTF(text)}}"

object rulesRTF: RulesBase() {
    override fun agentK(k: ChemK): String = escapeRTF("$k")
    override fun atom(id: String): String = id
    override fun comment(text: String): String = escapeRTF(text)
    override fun custom(text: String): String = escapeRTF(text)
    override fun itemCount(k: ChemK): String = subRTF("$k")
    override fun itemMass(mass: Double): String = supRTF(strMass(mass))
    override fun itemMassAndNum(mass: Double, number: Int): String =
            supRTF(strMass(mass)) + subRTF("$number")

    override fun nodeCharge(charge: ChemCharge): String = supRTF(charge.text)
    override fun operation(op: ChemOp): String {
        // Пока не удалось найти нормальную реализацию для размещения комментов над и под стрелкой
        var result = ""
        op.commentPre?.let { result += supRTF(it.text) }
        result += escapeRTF(op.dstText)
        op.commentPost?.let { result += subRTF(it.text) }
        return result
    }
    override fun radical(label: String): String = escapeRTF(label)
}

/*
Для поддержки цветов нужно вставить таблицу цветов
{\colortbl;\red0\green0\blue0;\red255\green0\blue0;}
Н.р, для красного цвета: {\cf2 this is red text}
 */