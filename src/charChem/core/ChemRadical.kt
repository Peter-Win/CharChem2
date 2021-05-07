package charChem.core

class ChemRadical(val label: String, val items: ElemList) : ChemSubObj() {
    override fun walk(visitor: Visitor) {
        visitor.radical(this)
    }

    companion object {
        val dict: Map<String, ChemRadical> by lazy {
            val radicalsList = mutableListOf<ChemRadical>()
            descriptions.forEach { descr ->
                val (left, right) = descr.split(':')
                val elemList = parseElemList(right)
                left.split(',').map { radicalsList.add(ChemRadical(it, elemList)) }
            }
            radicalsList.associateBy { it.label }
        }
        private val descriptions = listOf(
                "Me:C,H*3",
                "Et:C*2,H*5",
                "Ph:C*6,H*5",
                "Pr,n-Pr,Pr-n:C*3,H*7",
                "iPr,i-Pr,Pr-i:C*3,H*7",
                "Bu,nBu,n-Bu,Bu-n:C*4,H*9",
                "i-Bu,Bu-i:C*4,H*9",
                "Ac:C,H*3,C,O",
                "Tf:C,F*3,S,O*2", // TfOH = CF3SO3H, Tf = CF3SO2 https://en.wikipedia.org/wiki/Trifluoromethylsulfonyl
        // MsOH https://en.wikipedia.org/wiki/Methanesulfonic_acid
        )
    }
}

// descr example: "C*4,H*9"
fun parseElemList(descr: String): ElemList {
    val elemList = ElemList()
    descr.split(',').map { elemDescr ->
        val parts = elemDescr.split('*')
        if (parts.size == 1) {
            elemList.addElem(parts[0])
        } else {
            elemList.addElem(parts[0], parts[1].toDouble())
        }
    }
    return elemList
}
