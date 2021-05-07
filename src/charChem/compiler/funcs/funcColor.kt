package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

fun funcColor(
        compiler: ChemCompiler,
        args: List<String>,
        @Suppress("UNUSED_PARAMETER") pos: List<Int>
) {
    compiler.varColor = args.getOrNull(0)
}

fun funcItemColor(
        compiler: ChemCompiler,
        args: List<String>,
        @Suppress("UNUSED_PARAMETER") pos: List<Int>
) {
    compiler.varItemColor = args.getOrNull(0)
}

fun funcItemColor1(
        compiler: ChemCompiler,
        args: List<String>,
        @Suppress("UNUSED_PARAMETER") pos: List<Int>
) {
    compiler.varItemColor1 = args.getOrNull(0)
}

fun funcAtomColor(
        compiler: ChemCompiler,
        args: List<String>,
        @Suppress("UNUSED_PARAMETER") pos: List<Int>
) {
    compiler.varAtomColor = args.getOrNull(0)
}

fun funcAtomColor1(
        compiler: ChemCompiler,
        args: List<String>,
        @Suppress("UNUSED_PARAMETER") pos: List<Int>
) {
    compiler.varAtomColor1 = args.getOrNull(0)
}
