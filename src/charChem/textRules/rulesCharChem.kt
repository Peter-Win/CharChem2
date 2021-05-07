package charChem.textRules

import charChem.core.ChemCharge
import charChem.core.ChemComment
import charChem.core.ChemK
import charChem.core.ChemOp
import charChem.math.strMass

/*
Следует учесть, что это не полноценная система обратного преобразования выражения в текст формулы.
Здесь действуют те же ограничения, что и для остальных правил: формула должна быть текстовой(линейной)
 */

private fun exportCoeff(k: ChemK) = if (k.isNumber()) "$k" else "'$k'"

object rulesCharChem: RulesBase() {
    override fun agentK(k: ChemK): String = exportCoeff(k)
    override fun comment(text: String): String = "\"$text\""
    override fun custom(text: String): String = "{$text}"
    override fun itemCount(k: ChemK): String = exportCoeff(k)
    override fun itemMass(mass: Double): String = "\$M(${strMass(mass)})"
    override fun itemMassAndNum(mass: Double, number: Int): String = "\$nM(${strMass(mass)},$number)"
    override fun nodeCharge(charge: ChemCharge): String = "^${charge.text}"
    override fun operation(op: ChemOp): String = "${opComment(op.commentPre)}${op.srcText}${opComment(op.commentPost)}"
    override fun opComment(comm: ChemComment?): String = comm?.let { comment(it.text) } ?: ""
    override fun postProcess(text: String): String = text.replace("\$color()\$color", "\$color")
    override fun radical(label: String): String = custom(label)
    override fun mul(): String = "*"
    override fun mulK(k: ChemK): String = exportCoeff(k)
    override fun colorBegin(color: String): String = "\$color($color)"
    override fun colorEnd(): String = "\$color()"
}