package charChem.compiler

import charChem.core.ChemBond

fun createBond(compiler: ChemCompiler): ChemBond {
    val bond = ChemBond()
    bond.nodes[0] = compiler.getForcedNode(true)
    compiler.closeBond()
    compiler.closeNode()
    compiler.curAgent?.let { it.addBond(bond) }
    compiler.curBond = bond
    return bond
}