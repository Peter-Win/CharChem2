/**
 * Abstract component
 * For example: {R}-OH
 * Created by PeterWin on 29.04.2017.
 */
package charChem.core

class ChemCustom(val text: String): ChemSubObj() {
    override fun walk(visitor: Visitor) {
        visitor.custom(this)
    }
}