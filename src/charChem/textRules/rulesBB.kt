package charChem.textRules

import charChem.core.ChemCharge
import charChem.core.ChemK
import charChem.math.strMass

/**
 * Правила для формирования BB-кода представления формулы (для вставки в форумы)
 */
object rulesBB: RulesBase() {
    override fun agentK(k: ChemK): String = "[b]${k}[/b]"
    override fun comment(text: String): String = "[i]$text[/i]"
    override fun custom(text: String): String = "[i]$text[/i]"
    override fun itemCount(k: ChemK): String = "[sub]${k}[/sub]"
    override fun itemMass(mass: Double): String = "[sup]${strMass(mass)}[/sup]"
    override fun itemMassAndNum(mass: Double, number: Int): String =
            "[sup]${strMass(mass)}[/sup][sub]${number}[/sub]"
    override fun nodeCharge(charge: ChemCharge): String = "[sup]${charge.text}[/sup]"
    override fun colorBegin(color: String): String = "[color=$color]"
    override fun colorEnd(): String = "[/color]"
}