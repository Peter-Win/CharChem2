package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

fun funcDblAlign(
        compiler: ChemCompiler,
        args: List<String>,
        @Suppress("UNUSED_PARAMETER") pos: List<Int>
) {
    compiler.varAlign = if (args.isNotEmpty() && (args[0] in setOf("r", "R", "l", "L", "m", "M"))) {
        args[0][0].toLowerCase()
    } else {
        null
    }
}