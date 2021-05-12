package charChem.compiler.tests

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.math.Point
import org.testng.annotations.Test
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.test.assertEquals

class TestParseNum {
    @Test
    fun testUseVariable() {
        val expr = compile("_(x%width:1.2)_(y-%height:0.9)_(x-%width)_(y%height)")
        assertEquals(expr.getMessage(), "")
        val w = 1.2
        val h = 0.9
        assertEquals(expr.getAgents()[0].nodes.map { it.pt },
            listOf(Point(), Point(w, 0.0), Point(w, -h), Point(0.0, -h)))
        assertEquals(makeTextFormula(makeBrutto(expr)), "C4H8")
    }
    @Test
    fun testInvalidVariable() {
        val expr = compile("_(x%)")
        assertEquals(expr.getMessage("ru"),
                "Не определена числовая переменная '' в позиции 5")
    }
    @Test
    fun testUnusedVariable() {
        val expr = compile("_(A%phi)")
        assertEquals(expr.getMessage("ru"),
                "Не определена числовая переменная 'phi' в позиции 5")
    }
    @Test
    fun testEmptyVariable() {
        val expr = compile("_(A-%:90)")
        assertEquals(expr.getMessage("ru"),
                "Требуется указать имя переменной в позиции 6")
    }
    @Test
    fun testInvalidVariableValue() {
        val expr = compile("_(a%Angle:ABC)")
        assertEquals(expr.getMessage("ru"),
                "Неверное числовое значение ABC в позиции 11")
    }
    @Test
    fun testConst() {
        //     /\
        //   /    \
        // |        |
        // |________|
        val expr = compile("_(x\$32,y.5)|_(x-\$3)`|/")
        assertEquals(expr.getMessage(), "")
        assertEquals(makeTextFormula(makeBrutto(expr)), "C5H10")
        assertEquals(expr.getAgents()[0].bonds.map { it.dir!!.polarAngleDeg().roundToInt() },
                listOf(30, 90, 180, -90, -30))
        assertEquals(expr.getAgents()[0].nodes.map { it.pt }, listOf(
                Point(),
                Point(sqrt(3.0) / 2, 0.5),
                Point(sqrt(3.0) / 2, 1.5),
                Point(-sqrt(3.0) / 2, 1.5),
                Point(-sqrt(3.0) / 2, 0.5),
        ))
    }
}