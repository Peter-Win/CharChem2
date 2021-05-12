package charChem.compiler.funcs

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.parseNum

fun funcL(compiler: ChemCompiler, args: List<String>, pos: List<Int>) {
    compiler.varLength = if (args.isEmpty()) 1.0 else parseNum(compiler, args[0], pos[0])
}