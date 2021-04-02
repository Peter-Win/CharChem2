package charChem.compiler.tests

import charChem.compiler.ChemCompiler
import charChem.compiler.createBondShort
import charChem.core.ChemAgent
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestLoadSuffix {
    @Test
    fun testZero() {
        val c = ChemCompiler("/0C")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 2)
        assertEquals(bond.n, 0.0)
        assertEquals(bond.style, "")
    }
    @Test
    fun testHydrogen() {
        val c = ChemCompiler("/hH")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 2)
        assertEquals(bond.n, 0.0)
        assertEquals(bond.style, ":")
    }
    @Test
    fun testW() {
        val c = ChemCompiler("/w\\")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 2)
        assertEquals(bond.w0, 0)
        assertEquals(bond.w1, 1)
    }
    @Test
    fun testWW() {
        val c = ChemCompiler("/ww\\")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 3)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.w0, 1)
        assertEquals(bond.w1, 0)
    }
    @Test
    fun testD() {
        val c = ChemCompiler("/d\\")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 2)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.w0, 0)
        assertEquals(bond.w1, -1)
    }
    @Test
    fun testDD() {
        val c = ChemCompiler("/dd\\")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 3)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.w0, -1)
        assertEquals(bond.w1, 0)
        assertEquals(bond.style, "")
    }
    @Test
    fun testX() {
        val c = ChemCompiler("//x\\")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 3)
        assertEquals(bond.n, 2.0)
        assertEquals(bond.style, "x")
    }
    @Test
    fun testTilda() {
        val c = ChemCompiler("/~H")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 2)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.style, "~")
    }
    @Test
    fun testV() {
        val c = ChemCompiler("/vPb")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 2)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.style, "")
        assertEquals(bond.arr0, false)
        assertEquals(bond.arr1, true)
    }
    @Test
    fun testVV() {
        val c = ChemCompiler("/vvPb")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 3)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.style, "")
        assertEquals(bond.arr0, true)
        assertEquals(bond.arr1, false)
    }
    @Test
    fun testVVV() {
        val c = ChemCompiler("/vvvPb")
        c.prepareText()
        c.createEntity(ChemAgent())
        val bond = createBondShort(c)
        assertEquals(c.pos, 4)
        assertEquals(bond.n, 1.0)
        assertEquals(bond.style, "")
        assertEquals(bond.arr0, true)
        assertEquals(bond.arr1, true)
    }
}