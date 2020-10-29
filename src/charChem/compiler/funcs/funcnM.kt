package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

// Масса следующего элемента плюс атомный номер   238 #  #
// Например $nM(238)U                                 #  #
//                                                 92  ##
// Или можно $nM(1,0){n}

fun funcnM(compiler: ChemCompiler, args: List<String>, pos: List<Int>): Unit {
    funcM(compiler, args, pos)
    compiler.customAtomNumber = if (args.size > 1) {
        args[1].toIntOrNull()?: -1
    } else {
        -1
    }
}