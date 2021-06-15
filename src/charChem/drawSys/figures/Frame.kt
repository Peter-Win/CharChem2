package charChem.drawSys.figures

import charChem.math.Rect

class Frame(): Figure() {
    private val bounds = Rect()
    override fun getBounds(): Rect = bounds
}