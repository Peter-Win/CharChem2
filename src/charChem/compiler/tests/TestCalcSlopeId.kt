package charChem.compiler.tests

import charChem.compiler.calcSlopeId
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestCalcSlopeId {
    @Test
    fun testCalcSlopeId() {
        assertEquals(calcSlopeId(-1.0, false, false, true), 1)
        assertEquals(calcSlopeId(-1.0, false, false, false), 2)
        assertEquals(calcSlopeId(0.0, false, true, false), 3)
        assertEquals(calcSlopeId(1.0, false, false, false), 4)
        assertEquals(calcSlopeId(1.0, false, false, true), 5)
        assertEquals(calcSlopeId(0.0, false, false, false), 6)
        assertEquals(calcSlopeId(-1.0, true, false, true), 7)
        assertEquals(calcSlopeId(-1.0, true, false, false), 8)
        assertEquals(calcSlopeId(0.0, true, true, false), 9)
        assertEquals(calcSlopeId(1.0, true, false, false), 10)
        assertEquals(calcSlopeId(1.0, true, false, true), 11)
        assertEquals(calcSlopeId(0.0, true, false, false), 12)
    }
}