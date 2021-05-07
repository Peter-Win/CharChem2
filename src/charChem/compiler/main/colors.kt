package charChem.compiler.main

import charChem.compiler.ChemCompiler

fun getItemColor(compiler: ChemCompiler): String? =
        compiler.varItemColor1?.let {
            compiler.varItemColor1 = null
            it
        } ?: compiler.varItemColor ?: compiler.varColor

fun getAtomColor(compiler: ChemCompiler): String? =
        compiler.varAtomColor1?.let {
            compiler.varAtomColor1 = null
            it
        } ?: compiler.varAtomColor
