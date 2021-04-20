package charChem.compiler.parse

import charChem.compiler.ChemCompiler

fun parseNum(compiler: ChemCompiler, value: String, valuePos: Int): Double {
    return value.toDoubleOrNull()
            ?: compiler.error(
                    "Invalid number [n]",
                    listOf<Pair<String, Any>>("n" to value, "pos" to valuePos)
            )
}
