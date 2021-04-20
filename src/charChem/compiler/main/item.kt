package charChem.compiler.main

import charChem.compiler.ChemCompiler

fun closeItem(compiler: ChemCompiler) {
}

fun getLastItem(compiler: ChemCompiler) = compiler.curNode?.items?.lastOrNull()