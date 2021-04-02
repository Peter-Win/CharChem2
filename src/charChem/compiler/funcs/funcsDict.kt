package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

val funcsDict = mapOf<String, (ChemCompiler, List<String>, List<Int>)->Unit>(
        "M" to ::funcM,
        "nM" to ::funcnM,
        "slope" to ::funcSlope,
)