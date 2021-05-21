package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

val funcsDict = mapOf<String, (ChemCompiler, List<String>, List<Int>)->Unit>(
        "atomColor" to ::funcAtomColor,
        "atomColor1" to ::funcAtomColor1,
        "color" to ::funcColor,
        "dblAlign" to ::funcDblAlign,
        "itemColor" to ::funcItemColor,
        "itemColor1" to ::funcItemColor1,
        "L" to ::funcL,
        "M" to ::funcM,
        "nM" to ::funcnM,
        "slope" to ::funcSlope,
        "ver" to ::funcVer,
)