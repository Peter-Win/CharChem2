package charChem.compiler2.parse

import charChem.compiler2.ChemCompiler
import charChem.compiler2.main.OpDef
import charChem.compiler2.main.opsList

fun scanOp(compiler: ChemCompiler): OpDef? {
    val res = opsList.find{ compiler.isCurPosEq(it.src) }
    if (res != null) {
        compiler.pos += res.src.length
    }
    return res
}