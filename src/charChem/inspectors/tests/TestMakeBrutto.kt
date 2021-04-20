package charChem.inspectors.tests

import charChem.compiler.compile
import charChem.inspectors.calcMass
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestMakeBrutto {
    @Test
    fun testSingleNode() {
        val src = compile("CH3CH2OH")
        assertEquals(src.getMessage(), "")
        val brutto = makeBrutto(src)
        assertEquals(brutto.getMessage(), "")
        assertEquals(makeTextFormula(brutto), "C2H6O")
        assertEquals(calcMass(src), calcMass(brutto))
    }
}