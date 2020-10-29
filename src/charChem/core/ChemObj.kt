/**
 * Base class for all chemical objects
 * Supports information about the position of the object in the source description (usually after the preprocessor)
 * Created by PeterWin on 28.04.2017.
 */
package charChem.core

abstract class ChemObj {
    abstract fun walk(visitor: Visitor)

    fun walkExt(visitor: Visitor): Visitor {
        walk(visitor)
        return visitor
    }

    private var bounds: Pair<Int, Int>? = null
    fun setBounds(begin: Int, end: Int) {
        bounds = Pair(begin, end)
    }
    fun updateLastBound(end: Int) {
        bounds = Pair(bounds!!.first, end)
    }
    fun updateFirstBound(begin: Int) {
        bounds = Pair(begin, bounds!!.second)
    }
    fun getBoundsBegin(): Int {
        return bounds!!.first
    }
    fun getBoundsEnd(): Int {
        return bounds!!.second
    }
}