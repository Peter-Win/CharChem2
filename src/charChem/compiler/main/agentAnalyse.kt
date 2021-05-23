package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.state.*
import charChem.core.instChemComma

fun agentAnalyse(compiler: ChemCompiler, onDefault: () -> Int): Int {
    val c = compiler.curChar()
    val bond = scanSimpleBond(compiler)
    if (bond != null) {
        createSimpleBond(compiler, bond)
        return compiler.setState(::stateAgentMid)
    }
    return when (c) {
        in 'A'..'Z' -> {
            // Извлечь первый заглавный символ элемента. Следующие должны быть маленькими
            compiler.elementStartPos = compiler.pos
            compiler.setState(::stateElement, 1)
        }
        '`' -> {
            compiler.setAltFlag()
            compiler.setState(::stateAgentMid, 1)
        }
        '{' ->
            compiler.setState(::stateCustom, 1)
        '"' ->
            compiler.setState(::stateCommentIn, 1)
        ';' -> {
            closeChain(compiler)
            compiler.setState(::stateAgentSpace, 1)
        }
        ':' ->
            createLabel(compiler)
        '#' ->
            compiler.setState(::stateNodeRef, 1)
        '^' ->
            compiler.setState(::stateCharge, 1)
        '$' ->
            compiler.setState(::stateFuncName, 1)
        '<' ->
            openBranch(compiler)
        '>' ->
            closeBranch(compiler)
        '(' ->
            openParentheses(compiler)
        '[' ->
            openSquareBracket(compiler)
        ')', ']' ->
            closeBracketShort(compiler)
        '*' ->
            star(compiler)
        ',' ->
            comma(compiler)
        '_' ->
            compiler.setState(::stateUniBond, 1)
        'c' -> {
            getNodeForced(compiler, true)
            compiler.setState(::stateAgentMid, 1)
        }
        else -> onDefault()
    }
}

fun comma(compiler: ChemCompiler): Int {
    addNodeItem(compiler, instChemComma)
    return compiler.setState(::stateAgentMid, 1)
}