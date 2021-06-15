package charChem.drawSys

import charChem.drawSys.figures.Figure
import charChem.math.Rect

abstract class DrawSystem {
    fun createImageFromFigure(figure: Figure): ImageAbstract {
        val image = createImage(figure.getBounds())
        drawFigure(image, figure)
        return image
    }
    abstract fun createImage(bounds: Rect): ImageAbstract
    abstract fun drawFigure(image: ImageAbstract, figure: Figure)
}