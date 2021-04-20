package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.main.createAgent
import charChem.compiler.parse.scanCoeff

fun stateAgent(compiler: ChemCompiler): Int {
    val agent = createAgent(compiler)

    // TODO: Пока нет множителей, используем упрощенное предположение, что коэффициент только один
    scanCoeff(compiler)?.let { coeff -> agent.n = coeff }

    return compiler.setState(::stateAgentIn)
}