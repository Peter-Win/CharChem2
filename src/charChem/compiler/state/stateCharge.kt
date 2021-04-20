package charChem.compiler.state

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.scanCharge

fun stateCharge(compiler: ChemCompiler): Int {
    val pos = compiler.pos
    val chargeOwner = compiler.chargeOwner
            ?: compiler.error("Expected node declaration before charge", listOf("pos" to pos - 1))
    // Наличие ` перед объявлением заряда означает, что заряд нужно вывести слева
    val isLeft = compiler.getAltFlag()
    chargeOwner.charge = scanCharge(compiler, isLeft)
            ?: compiler.error("Invalid charge declaration", listOf("pos" to pos))
    return compiler.setState(::stateAgentMid)
}