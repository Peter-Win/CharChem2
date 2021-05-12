package charChem.compiler.parse

import charChem.compiler.ChemCompiler
import charChem.core.ChemBond

/*
Суффиксы позволяют указать одну из следующих модификаций для только что объявленной связи.
Во всех версиях используется с краткими описаниями связей.
Начиная с версии 2.0 может применяться к полигональным связям
 */

private data class SuffixDef(val suffix: String, val action: (bond: ChemBond) -> Unit)

private val bondSuffixes = listOf<SuffixDef>(
        SuffixDef("0"){ bond -> bond.n = 0.0 },
        SuffixDef("o"){ bond -> bond.n = 0.0 },
        SuffixDef("h"){ bond -> bond.setHydrogen() },
        SuffixDef("ww"){ bond -> bond.w0 = 1 },
        SuffixDef("w"){ bond -> bond.w1 = 1 },
        SuffixDef("dd"){ bond -> bond.w0 = -1 },
        SuffixDef("d"){ bond -> bond.w1 = -1 },
        SuffixDef("x"){ bond -> bond.setCross() },
        SuffixDef("~"){ bond -> bond.style = "~" },
        SuffixDef("vvv"){ bond ->
            bond.arr0 = true
            bond.arr1 = true
        },
        SuffixDef("vv"){ bond -> bond.arr0 = true },
        SuffixDef("v"){ bond -> bond.arr1 = true },
)

fun scanBondSuffix(compiler: ChemCompiler, bond: ChemBond) {
    bondSuffixes.find { compiler.isCurPosEq(it.suffix) } ?. let {
        it.action(bond)
        compiler.pos += it.suffix.length
    }
}