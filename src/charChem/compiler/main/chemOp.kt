package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemOp

class OpDef(val src: String, val dst: String? = null, val div: Boolean = false) {
    fun opCode() = dst ?: src
}

val opsList = listOf(
    OpDef("+"),
    OpDef("-->", "—→", true),
    OpDef("->", "→", true),
    OpDef("®", "→", true),
    OpDef("→", null, true),
    OpDef("=", null, true),
    OpDef("↔", null, true),
    OpDef("<->", "↔", true),
    OpDef("<=>", "\u21CC", true),
    OpDef("<==>", "\u21CC", true),
    OpDef("*", "∙"),
    OpDef("!=", "≠", true),
)

fun onCloseOp(compiler: ChemCompiler) {
    compiler.curOp = null
}

fun createChemOp(compiler: ChemCompiler, def: OpDef) {
    val preComm = compiler.preComm
    closeEntity(compiler)
    val op = ChemOp(def.src, def.opCode(), def.div)
    op.commentPre = preComm
    onCreateEntity(compiler, op)
    compiler.curOp = op
    if (def.div) {
        compiler.curPart++
    }
}