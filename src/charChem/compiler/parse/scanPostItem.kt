package charChem.compiler.parse

import charChem.compiler.ChemCompiler
import charChem.core.ChemK

fun scanPostItem(
        compiler: ChemCompiler,
        onCoeff: (coeff: ChemK) -> Unit,
) {
    scanCoeff(compiler)?.let { onCoeff(it) }
}