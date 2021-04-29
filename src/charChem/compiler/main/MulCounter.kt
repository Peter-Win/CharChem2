package charChem.compiler.main

import charChem.core.ChemMul

/*
 Множители появляются:
 - после символа *;
 - сразу после открытия скобки (это новая фича для версии 1.2)
 Закрываются автоматически:
 - перед символом *
 - перед закрытием СООТВЕТСТВУЮЩЕЙ скобки
 - в конце агента
 */

class MulCounter() {
    private var mul: ChemMul? = null
    private var bracketCounter: Int = 0
    fun onOpenBracket() = mul?.let { ++bracketCounter }
    fun onCloseBracket() = mul?.let { --bracketCounter }
    fun close() {
        mul = null
    }
    fun create(newMul: ChemMul) {
        mul = newMul
    }
    fun getMulForBracket(): ChemMul? = if (bracketCounter > 0) null else mul
    fun getMulForced(): ChemMul? = mul
}