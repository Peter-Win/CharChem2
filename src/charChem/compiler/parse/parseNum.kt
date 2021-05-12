package charChem.compiler.parse

import charChem.compiler.ChemCompiler
import kotlin.math.sqrt

private val numConstDict = mapOf<String, Double>(
        "\$32" to sqrt(3.0) /2,
        "\$3" to sqrt(3.0),
        "\$3x2" to sqrt(3.0)*2,
        "\$2" to sqrt(2.0),
        "\$22" to sqrt(2.0)/2,
        "\$2x2" to sqrt(2.0)*2,
        "½" to 0.5,
        "¼" to 1.0/4,
        "¾" to 3.0/4,
        "⅓" to 1.0/3,
        "⅔" to 2.0/3,
)

private fun invalidNumber(compiler: ChemCompiler, value: String, pos: Int): Nothing {
    compiler.error(
            "Invalid number [n]",
            listOf("n" to value, "pos" to pos)
    )
}

private fun parseNumConst(compiler: ChemCompiler, value: String, pos: Int): Double {
    return numConstDict[value] ?: invalidNumber(compiler, value, pos)
}

private fun useVariable(compiler: ChemCompiler, name: String, pos: Int): Double {
    return compiler.varsDict[name] ?: compiler.error(
            "Undefined variable [name]",
            listOf("name" to name, "pos" to pos),
    )
}

private fun declareVariable(compiler: ChemCompiler, name: String, value: String, pos: Int): Double {
    if (name.isEmpty()) {
        compiler.error("Expected variable name", listOf("pos" to pos))
    }
    val n: Double = value.toDoubleOrNull() ?: parseNumConst(compiler, value, pos + name.length + 1)
    compiler.varsDict[name] = n
    return n
}

private fun parseVariable(compiler: ChemCompiler, expr: String, pos: Int): Double {
    val k = expr.indexOf(':')
    return if (k < 0) useVariable(compiler, expr, pos) else
        declareVariable(compiler, expr.substring(0, k), expr.substring(k+1), pos)
}

private fun parseNumExt(compiler: ChemCompiler, srcValue: String, valuePos: Int): Double {
    var k: Double = 1.0
    var value: String = srcValue
    var curPos = valuePos
    if (srcValue.startsWith('-')) {
        k = -1.0
        curPos++
        value = value.substring(1)
    }
    if (value.startsWith('%')) {
        return parseVariable(compiler, value.substring(1), curPos+1) * k
    }
    return parseNumConst(compiler, value, curPos) * k
}

fun parseNum(compiler: ChemCompiler, value: String, pos: Int): Double {
    if (value.isEmpty()) return 0.0
    return value.toDoubleOrNull() ?: parseNumExt(compiler, value, pos)
}
