package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

const val degToRad = Math.PI / 180.0

fun funcSlope(compiler: ChemCompiler, args: List<String>, pos: List<Int>): Unit {
    compiler.userSlope = if (args.isEmpty()) 0.0
        else args[0].toDoubleOrNull() ?.let { it * degToRad } ?: 0.0
}