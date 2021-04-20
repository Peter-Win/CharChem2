package charChem.compiler.funcs

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.parseNum

fun funcSlope(compiler: ChemCompiler, args: List<String>, pos: List<Int>) {
    compiler.varSlope = if (args.isEmpty()) 0.0 else parseNum(compiler, args[0], pos[0])
}