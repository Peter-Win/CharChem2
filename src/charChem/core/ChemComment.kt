/**
 * Comment
 * For example: "Anion"-SO4^2-
 * Created by PeterWin on 29.04.2017.
 */
package charChem.core

class ChemComment(val text: String): ChemSubObj() {
    override fun walk(visitor: Visitor) {
        visitor.comment(this)
    }
}