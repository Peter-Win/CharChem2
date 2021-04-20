package charChem.compiler.funcs

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.parseNum

fun funcM(compiler: ChemCompiler, args: List<String>, pos: List<Int>) {
    if (args.isNotEmpty()) {
        compiler.varMass = parseNum(compiler, args[0], pos[0])
    }
}