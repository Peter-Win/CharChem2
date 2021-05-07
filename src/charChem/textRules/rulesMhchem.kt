package charChem.textRules

import charChem.core.ChemCharge
import charChem.core.ChemK
import charChem.core.ChemOp
import charChem.math.strMass

// MathJax/mhchem
// https://mhchem.github.io/MathJax-mhchem/

object rulesMhchem: RulesBase() {
    override fun agentK(k: ChemK): String = "$k" // Возможно, нужны разные правила для чисел, дробных чисел и абстрактных значений
    override fun itemCount(k: ChemK): String = "_{$k}"
    override fun itemMass(mass: Double): String = "^{${strMass(mass)}}"
    override fun itemMassAndNum(mass: Double, number: Int): String =
            "^{${strMass(mass)}}_{$number}"
    override fun nodeCharge(charge: ChemCharge): String = "^{${charge.text}}"
    override fun operation(op: ChemOp): String {
        var result = when (op.srcText) {
            "-->" -> "->"
            "<=>" -> "<-->"
            "<==>" -> "<-->"
            else -> op.srcText
        }
        val t1 = op.commentPre
        val t2 = op.commentPost
        if (t1 != null || t2 != null) {
            result += "[{${t1?.text ?: ""}}]"
        }
        t2?.let { result += "[{${it.text}}]" }
        return result
    }
    override fun mul() = "*"
    override fun colorBegin(color: String): String = "\\color{$color}{"
    override fun colorEnd() = "}"
}