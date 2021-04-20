package charChem.compiler.parse

import charChem.compiler.ChemCompiler
import charChem.core.ChemComment
import charChem.lang.Lang

fun createComment(compiler: ChemCompiler): ChemComment {
    val src = scanComment(compiler)
    val dst = convertComment(src)
    compiler.pos++
    return ChemComment(dst)
}

/** Извлекает комментарий
 * Изначально pos должен быть установлен на первый символ внутри кавычек
 * В конце он установлен на завершающую кавычку
 */
fun scanComment(compiler: ChemCompiler): String {
    val pos0 = compiler.pos
    if (!scanTo(compiler, '"')) {
        compiler.error("Comment is not closed", listOf("pos" to pos0 - 1))
    }
    return compiler.subStr(pos0)
}

fun replaceLimited(text: String, firstLimiter: Char, lastLimiter: Char, transform: (String) -> String? ): String {
    var i = 0
    var result = text
    while (i < result.length) {
        val beginPos = result.indexOf(firstLimiter, i)
        if (beginPos < 0 || beginPos == result.length) break
        val endPos = result.indexOf(lastLimiter, beginPos + 1)
        if (endPos < 0) break
        val key = result.substring(beginPos+1, endPos)
        val value = transform(key)
        if (value != null) {
            // соответствие найдено. Выполняем замену
            result = result.substring(0, beginPos) + value + result.substring(endPos + 1)
            i = beginPos + value.length
        } else {
            // соответствие не найдено. Оставляем скобки в тексте
            i = beginPos + 1
        }
    }
    return result
}


// Выполнить подстановку специальных символов:
// - часто встречающиеся символы типа градуса или стрелки.
// - символы в квадратных скобках. (греческие буквы)
// - фразы для локализации в обратных апострофах

fun convertComment(text: String): String {
    // замена частых символов
    var result: String = specChars.fold(text) {
        acc, pair ->  acc.replace(pair.first, pair.second)
    }
    // замена символов в квадратных скобках.
    result = replaceLimited(result, '[', ']') { specCharsB[it] }
    // Перевод фраз из словаря
    result = replaceLimited(result, '`', '`') {
        Lang.findPhrase(it) ?: it
    }
    return result
}

// Часто встречающиеся символы.
// Внедряются в текст без каких-то синтаксических ограничителей.
// Этот список не рекомендуется расширять, т.к. это снижает производительность
val specChars = listOf(
        "|^" to "↑",
        "ArrowUp" to "↑",
        "|v" to "↓",
        "ArrowDown" to "↓",
        "^o" to "°",
)

// Символы в квадратных скобках.
// Здесь можно добавлять другие символы без снижения производительности.
// (на производительность влияет размер текста и количество скобок в нем)

val specCharsB = mapOf(
        "alpha" to "α", "Alpha" to "Α",
        "beta" to "β", "Beta" to "Β",
        "gamma" to "γ", "Gamma" to "Γ",
        "delta" to "δ", "Delta" to "Δ",
        "epsilon" to "ε", "Epsilon" to "Ε",
        "zeta" to "ζ", "Zeta" to "Ζ",
        "eta" to "η", "Eta" to "Η",
        "theta" to "θ", "Theta" to "Θ",
        "iota" to "ι", "Iota" to "Ι",
        "kappa" to "κ", "Kappa" to "Κ",
        "lambda" to "λ", "Lambda" to "Λ",
        "mu" to "μ", "Mu" to "Μ",
        "nu" to "ν", "Nu" to "Ν",
        "xi" to "ξ", "Xi" to "Ξ",
        "omicron" to "ο", "Omicron" to "Ο",
        "pi" to "π", "Pi" to "Π",
        "rho" to "ρ", "Rho" to "Ρ",
        "sigma" to "σ", "Sigma" to "Σ",
        "tau" to "τ", "Tau" to "Τ",
        "upsilon" to "υ", "Upsilon" to "Υ",
        "phi" to "φ", "Phi" to "Φ",
        "chi" to "χ", "Chi" to "Χ",
        "psi" to "ψ", "Psi" to "Ψ",
        "omega" to "ω", "Omega" to "Ω",
)
