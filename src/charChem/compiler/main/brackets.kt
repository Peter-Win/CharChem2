package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.scanPostItem
import charChem.compiler.state.stateAgentMid
import charChem.core.ChemBracketBegin
import charChem.core.ChemBracketEnd
import charChem.core.ChemNode
import charChem.inspectors.makeTextFormula

/*

Скобки вкладываются совместно с ветками. То есть, если открыта ветка, а в ней скобка,
то сначала закрывается скобка, а потом ветка.

У скобок могут быть входящая и исходящая связь, но не обязательно.
(NH4)2SO4 - нет связей

H-(CH2)4-H - Есть обе связи. Причем они мягкие. Поэтому рисуются не к узлу, а к скобке.

H^+_(x1.4,N0)[Cu^+<`|hBr^-><|hBr^->]^-
     +  Br +
     |  |  |
  H--|--Cu |
     |  |  |
     +  Br +
Здесь есть только входящая связь, соединяющая узел снаружи и внутри скобки
 */

class BracketDecl(pos: Int, val begin: ChemBracketBegin) : StackItem(pos) {
    override fun msgInvalidClose(): String = "It is necessary to close the bracket"
}

fun openParentheses(compiler: ChemCompiler): Int {
    // Здесь возможны следующие случаи
    // - (* - открыть ветку
    // - (charge) - степень окисления текущего элемента узла
    // - скобка для конструкций типа Ca(OH)2
    compiler.pos++
    if (compiler.curChar() == '*') {
        return openBranch(compiler)
    }
    openBracket(compiler, "(", compiler.pos - 1)
    return compiler.setState(::stateAgentMid)
}

fun openSquareBracket(compiler: ChemCompiler): Int {
    openBracket(compiler, "[", compiler.pos++)
    return compiler.setState(::stateAgentMid)
}

fun openBracket(compiler: ChemCompiler, text: String, pos: Int) {
    val begin = ChemBracketBegin(text)
    compiler.curAgent!!.commands.add(begin)
    compiler.push(BracketDecl(pos, begin))
    if (compiler.curBond != null) {
        begin.bond = compiler.curBond
    } else {
        closeNode(compiler)
    }
}

val bracketPairs = mapOf("(" to ")", "[" to "]")

fun getNodeForBracketEnd(compiler: ChemCompiler): ChemNode {
    val curNode = compiler.curNode
    if (curNode != null) {
        return curNode
    }
    val lastCmd = compiler.curAgent!!.commands.lastOrNull()
    if (lastCmd is ChemBracketEnd) {
        return lastCmd.nodeIn
    }
    return openNode(compiler, true)
}

fun closeBracket(compiler: ChemCompiler, text: String, pos: Int): ChemBracketEnd {
    compiler.pop() ?. let { decl ->
        if (decl is BracketDecl) {
            val needCloseText: String = bracketPairs[decl.begin.text] ?:
                // Такая ошибка не должна возникнуть, если правильно заполнен словарь bracketPairs
                compiler.error("Invalid bracket pair [s]", listOf("s" to decl.begin.text + text))
            if (needCloseText != text) {
                // Тип открытой скобки должен соответствовать типу закрытой
                compiler.error("Expected [must] instead of [have]", listOf(
                        "must" to needCloseText,
                        "have" to text,
                        "pos" to pos,
                        "pos0" to decl.pos,
                ))
            }

            val bracketEnd = ChemBracketEnd(text, decl.begin, getNodeForBracketEnd(compiler))
            val commands = compiler.curAgent!!.commands
            commands.add(bracketEnd)
            decl.begin.end = bracketEnd
            closeNode(compiler)
            compiler.chargeOwner = bracketEnd
            return bracketEnd
        } else {
            compiler.error("Cant close bracket before branch",
            listOf("pos" to pos, "pos0" to decl.pos + 1))
        }
    } ?: compiler.error("Invalid bracket close", listOf("pos" to pos))
}

fun closeBracketShort(compiler: ChemCompiler): Int {
    val end = closeBracket(compiler, compiler.curChar().toString(), compiler.pos++)
    scanPostItem(compiler) { end.n = it }
    return compiler.setState(::stateAgentMid)
}