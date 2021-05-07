package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemK
import charChem.core.ChemMul
import charChem.core.ChemMulEnd

fun startMul(compiler: ChemCompiler, k: ChemK, isFirst: Boolean) {
    val mul = ChemMul(k, isFirst, compiler.varColor)
    closeNode(compiler)
    compiler.chainSys.closeSubChain()
    compiler.curAgent!!.commands.add(mul)
    compiler.mulCounter.create(mul)
}
fun stopMul(compiler: ChemCompiler, mul: ChemMul) {
    compiler.mulCounter.close()
    compiler.curAgent!!.commands.add(ChemMulEnd(mul))
}
fun checkMulBeforeBracket(compiler: ChemCompiler) {
    compiler.mulCounter.getMulForBracket()?.let { stopMul(compiler, it) }
}
fun checkMul(compiler: ChemCompiler) {
    compiler.mulCounter.getMulForced()?.let { stopMul(compiler, it) }
}