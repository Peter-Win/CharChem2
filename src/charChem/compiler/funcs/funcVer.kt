package charChem.compiler.funcs

import charChem.compiler.ChemCompiler
import charChem.currentVersion

fun toIntOrZero(s: String): Int {
    return try { s.toInt() } catch (e: NumberFormatException) { 0 }
}

// Эта функция должна обеспечивть совместимость с предыдущими версиями
// Возможно передать в одном параметре оба номера через точку 1.1
// Но можно и через запятую. Тогда это два параметра
fun parseVerParameter(args: List<String>): List<Int> {
    val a = args.getOrNull(0)
    val b = args.getOrNull(1)
    val verStr: String = if (a != null) {
        if (b != null) "$a.$b" else a
    } else {
        b ?: "0.0"
    }
    val verList = verStr.split(".")
    val verList2 = listOf(
            if (verList.isEmpty()) "0" else verList[0],
            if (verList.size < 2) "0" else verList[1]
    )
    return listOf(toIntOrZero(verList2[0]), toIntOrZero(verList2[1]))
}

fun funcVer(compiler: ChemCompiler,
            args: List<String>,
            @Suppress("UNUSED_PARAMETER") pos: List<Int>,
) {
    val (high, low) = parseVerParameter(args)
    if (high > currentVersion[0] || (high == currentVersion[0] && low > currentVersion[1])) {
        compiler.error("Invalid version", listOf(
                "cur" to "${currentVersion[0]}.${currentVersion[1]}",
                "need" to "$high.$low",
        ))
    }
}