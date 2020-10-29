package charChem.compiler

import charChem.compiler.state.stateBegin
import charChem.core.ChemComment
import charChem.core.ChemOp

data class OpDef(
        val op: String,
        val eq: Boolean = false,
        val dst: String? = null
)


val Ops: List<OpDef> = listOf(
        OpDef("+"),
        OpDef("-->", true, "—→"),
        OpDef("->", true, "→"),
        OpDef("®", true, "→"),
        OpDef("→", true),
        OpDef("=", true),
        OpDef("↔", true),
        OpDef("<->", true, "↔"),
        OpDef("<=>", true, "\u21CC"),
        OpDef("<==>", true, "\u21CC"),
        OpDef("*", false, "∙"), // deprecated
        OpDef("!=", false, "≠"),
)


fun parseOperation(compiler: ChemCompiler): Int {
    val pos = compiler.pos
    val text = compiler.text
    val def = Ops.find { text.indexOf(it.op, pos) == pos } ?: return -1
    // После операции нужен пробельный символ или начало нижнего коммента
    val pos1 = pos + def.op.length
    if (pos1 < text.length) {
        val c1 = text[pos1]
        if (!isSpace(c1) && c1 != '"')
            return -1
    }
    val op = ChemOp(def.op, def.dst ?: def.op, def.eq)
    op.setBounds(pos, pos1)
    compiler.createEntity(op)
    compiler.commentPre?.let {
        op.commentPre = it
        op.updateFirstBound(it.getBoundsBegin())
        compiler.commentPre = null
    }
    if (op.eq) {
        compiler.curPart++
    }
    compiler.pos = pos1
    if (!compiler.isFinish() && compiler.curChar() == '"') {
        // Начать читать нижний комментарий
        compiler.pos++
        val converted = convertComment(scanComment(compiler))
        compiler.pos++
        val comm = ChemComment(converted)
        comm.setBounds(pos1, compiler.pos)
        op.commentPost = comm
        op.updateLastBound(compiler.pos)
    }
    // начать считывание следующей сущности
    return compiler.setState(::stateBegin)
}