package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.scanCoeff
import charChem.core.ChemAgent

// Начало распознавания реагента
fun stateAgent(compiler: ChemCompiler): Int {
    // print("stateAgent. c='${compiler.curChar()}'\n")
    compiler.createEntity(ChemAgent())
    // Возможен предварительный коэффициент
    val k = scanCoeff(compiler)
    if (k != null) {
        // Если коэффициент есть, то в данный момент мы не знаем точно, относится ли он к агенту
        // или к компоненту аддукта или сольвата.
        // Поэтому пока просто сохраняем этот коэффициент
        compiler.prefixK = k
    }
    return compiler.setState(::stateAgentIn)
}