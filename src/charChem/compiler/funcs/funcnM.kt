package charChem.compiler.funcs

import charChem.compiler.ChemCompiler

// Масса следующего элемента плюс атомный номер   238 #  #
// Например $nM(238)U                                 #  #
//                                                 92  ##
// Или можно $nM(1,0){n}

fun funcnM(compiler: ChemCompiler, args: List<String>, pos: List<Int>) {
    funcM(compiler, args, pos)
    // -1 означает, что надо использовать массу того атома, к которому применяется функция
    // в отличие от null, который означает, что номер элемента вообще не выводится
    compiler.varAtomNumber = if (args.size > 1) {
        args[1].toIntOrNull()?: -1
    } else {
        -1
    }
}