package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

val funcsDict = mapOf<String, (ChemCompiler, List<String>, List<Int>)->Unit>(
        "L" to ::funcL,
        "M" to ::funcM,
        "nM" to ::funcnM,
        "slope" to ::funcSlope,
        "ver" to ::funcVer,
)