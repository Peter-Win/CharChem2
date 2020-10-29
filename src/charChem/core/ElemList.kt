/**
 * Elements list
 * Each element is record {id, elem, n}
 * For abstract  elem is null
 * Created by PeterWin on 29.04.2017.
 */
package charChem.core

import charChem.math.toa
import kotlin.math.abs

class ElemRec(val id: String, val elem: ChemAtom?, var n: Double = 1.0) {
    constructor(id: String, n: Double = 1.0) : this(id, findElement(id), n)
    constructor(atom: ChemAtom, n: Double = 1.0) : this(atom.id, atom, n)
    constructor(src: ElemRec, n: Double = 1.0) : this(src.id, src.elem, n * src.n)

    val key: String
        get() = if (elem != null) id else "{$id}"
}

fun k2s(k: Double): String = if (k == 1.0) "" else toa(k)

class ElemList(
        val list: MutableList<ElemRec> = mutableListOf(),
        var charge: Double = 0.0
) {
    override fun toString(): String {
        var result: String = list.fold("") { acc, elemRec -> "$acc${elemRec.key}${k2s(elemRec.n)}" }
        if (charge != 0.0) {
            val absCharge = abs(charge)
            val value = if (absCharge == 1.0) "" else toa(absCharge)
            val sign = if (charge < 0) "-" else "+"
            result += "^$value$sign"
        }
        return result
    }

    fun findElem(atom: ChemAtom?): ElemRec? =
            if (atom == null) null else list.find { it.elem == atom }

    // Example: list.findElem("He")
    fun findElem(id: String): ElemRec? = findElem(findElement(id))

    fun findCustom(id: String) = list.find { it.elem == null && it.id == id }

    fun findKey(key: String) = list.find { it.key == key }

    fun findRec(rec: ElemRec) = if (rec.elem != null) findElem(rec.elem) else findCustom(rec.id)

    private fun addElemRec(rec: ElemRec): ElemList {
        val foundRec = findRec(rec)
        if (foundRec == null) {
            list.add(rec)
        } else {
            foundRec.n += rec.n
        }
        return this
    }

    fun addElem(id: String, n: Double = 1.0) = addElemRec(ElemRec(id, n))
    fun addElem(atom: ChemAtom, n: Double = 1.0) = addElemRec(ElemRec(atom, n))
    fun addElem(elem: ElemRec, n: Double = 1.0) = addElemRec(ElemRec(elem, n))
    fun addCustom(text: String, n: Double = 1.0) = addElemRec(ElemRec(text, null, n))
    fun addList(srcList: ElemList): ElemList {
        srcList.list.forEach { addElem(it) }
        charge += srcList.charge
        return this
    }

    fun addRadical(radical: ChemRadical) = addList(radical.items)

    fun scale(k: Double): Unit {
        if (k != 1.0) {
            charge *= k
            list.forEach { it.n *= k }
        }
    }

    // sort by Hill system
    fun sortByHill(): Unit {
        list.sortWith(object : Comparator<ElemRec> {
            override fun compare(a: ElemRec, b: ElemRec): Int {
                val aid: String = a.id
                val bid: String = b.id
                if (a.elem == null && b.elem == null)
                    return aid.compareTo(bid)
                if (a.elem == null)
                    return 1
                if (b.elem == null)
                    return -1
                if (aid == bid)
                    return 0
                if (aid == "C")
                    return -1
                if (bid == "C")
                    return 1
                if (aid == "H")
                    return -1
                if (bid == "H")
                    return 1
                return aid.compareTo(bid)
            }
        })
    }
}

