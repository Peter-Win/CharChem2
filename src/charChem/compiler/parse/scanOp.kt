package charChem.compiler.parse

import charChem.compiler.ChemCompiler
import charChem.compiler.main.OpDef
import charChem.compiler.main.opsList

fun scanOp(compiler: ChemCompiler): OpDef? {
    val res = opsList.find{ compiler.isCurPosEq(it.src) }
    if (res != null) {
        compiler.pos += res.src.length
    }
    return res
}