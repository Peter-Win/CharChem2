package charChem.core

import charChem.lang.Lang
import charChem.lang.LangParams

class ChemError(val msgId: String, val params: LangParams?) : Throwable() {
    override val message: String
        get() = Lang.tr(msgId, params)
}