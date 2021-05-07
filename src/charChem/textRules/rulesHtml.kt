package charChem.textRules

import charChem.core.ChemCharge
import charChem.core.ChemComment
import charChem.core.ChemK
import charChem.core.ChemOp
import charChem.math.strMass

fun htmlEscape(text: String): String = text.replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")

/**
 * Правила для формирования HTML-представления формулы
 * Важно учесть, что полученная разметка предполагает использование определенных правил CSS
 * Их можно скачать тут
 *   http://charchem.org/download/charchem.css
 *
 * Различные части формулы оборачиваются специальными тегами, чтобы их было проще кастомизировать.
 * <b/> - Коэффициент агента
 * <em/> - Текстовый комментарий
 * <i/> - Абстрактные функциональные группы, н.р. {R}OH
 * <sub/> - Подстрочные коэффициенты
 * <span class="echem-op"/> - Операции в химических выражениях
 */
object rulesHtml: RulesBase() {
    override fun agentK(k: ChemK): String = "<b>${k.toString()}</b>"
    override fun comment(text: String): String = "<em>$text</em>"
    override fun custom(text: String): String = "<i>$text</i>"
    override fun itemCount(k: ChemK): String = "<sub>${k.toString()}</sub>"
    override fun itemMass(mass: Double): String = "<sup>${strMass(mass)}</sup>"
    override fun itemMassAndNum(mass: Double, number: Int): String =
            """<span class="echem-mass-and-num">${strMass(mass)}<br/>$number</span>"""
    override fun nodeCharge(charge: ChemCharge): String = "<sup>${charge.text}</sup>"
    override fun operation(op: ChemOp): String {
        var result = """<span class="echem-op">"""
        result += opComment(op.commentPre)
        result += when (op.srcText) {
            "-->" -> """<span class="chem-long-arrow">→</span>"""
            "<==>" -> "<span class=\"chem-long-arrow\">\u21CC</span>"
            else -> op.dstText
        }
        result += opComment(op.commentPost)
        return "$result</span>"
    }

    override fun opComment(comm: ChemComment?): String =
            comm?.let { """<span class="echem-opcomment">${it.text}</span>""" } ?: ""

    override fun colorBegin(color: String): String =
            """<span style="color:${htmlEscape(color)}">"""
    override fun colorEnd(): String = "</span>"
}