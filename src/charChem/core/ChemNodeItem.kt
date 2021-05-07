package charChem.core

class ChemNodeItem(val obj: ChemSubObj, var n: ChemK = ChemK(1)) : ChemObj() {
    var charge: ChemCharge? = null

    // Special mass.
    // If specified, then ignore mass of sub object
    var mass: Double = 0.0

    var atomNum: Int? = null	// признак вывода атомного номера (для ядерных реакций).
    var color: String? = null	// общий цвет
    var atomColor: String? = null	// цвет атомов
    //this.bCenter = 0;	// Необяз. признак приоритетности элемента, задаваемый при помощи обратного апострофа: H3C`O|
    //this.dots = [];
    //this.dashes = [];

    override fun walk(visitor: Visitor) {
        visitor.itemPre(this)
        if (!visitor.isStop)
            obj.walk(visitor)
        if (!visitor.isStop)
            visitor.itemPost(this)
    }
}