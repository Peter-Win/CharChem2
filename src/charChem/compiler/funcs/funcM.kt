package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

fun funcM(compiler: ChemCompiler, args: List<String>, pos: List<Int>): Unit {
    if (args.isNotEmpty()) {
        compiler.specMass = compiler.parseNum(args[0], pos[0])
    }
}