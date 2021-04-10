package charChem.compiler2.main

import charChem.compiler2.ChemCompiler

fun closeItem(compiler: ChemCompiler) {
}

fun getLastItem(compiler: ChemCompiler) = compiler.curNode?.items?.lastOrNull()