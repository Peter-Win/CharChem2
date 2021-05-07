package charChem.textRules

import charChem.core.ChemCharge
import charChem.core.ChemComment
import charChem.core.ChemK
import charChem.core.ChemOp
import charChem.math.strMass

open class RulesBase {
    open fun agentK(k: ChemK): String = k.toString()
    open fun atom(id: String): String = id
    open fun comma(): String = ","
    open fun comment(text: String): String = text
    open fun custom(text: String): String = text
    open fun itemCount(k: ChemK): String = k.toString()
    open fun itemMass(mass: Double): String = strMass(mass)
    open fun itemMassAndNum(mass: Double, number: Int) = ""
    open fun nodeCharge(charge: ChemCharge) = charge.text
    open fun operation(op: ChemOp): String = "${opComment(op.commentPre)}${op.dstText}${opComment(op.commentPost)}"
    open fun opComment(comm: ChemComment?): String = comm?.let { comm.text } ?: ""
    open fun postProcess(text: String): String = text
    open fun radical(label: String): String = label
    open fun mul(): String = "∙"
    open fun mulK(k: ChemK) = "$k"
    open fun colorBegin(color: String): String = ""
    open fun colorEnd(): String = ""
}