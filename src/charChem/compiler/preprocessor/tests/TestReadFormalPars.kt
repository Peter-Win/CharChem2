package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.PreProcCtx
import charChem.compiler.preprocessor.readFormalPars
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestReadFormalPars {
    @Test
    fun testEmpty() {
        val p = readFormalPars(PreProcCtx("A() ", 2))
        assertEquals(p.dict.size, 0)
        assertEquals(p.names.size, 0)
    }
    @Test
    fun testSingle() {
        val p = readFormalPars(PreProcCtx("A(abc) ", 2))
        assertEquals(p.names, listOf("abc"))
        assertEquals(p.dict.size, 1)
        assertEquals(p.dict["abc"], "")
    }
    @Test
    fun testWithDefaultValues() {
        val p = readFormalPars(PreProcCtx("first:1,second:2)", 0))
        assertEquals(p.names, listOf("first", "second"))
        assertEquals(p.dict.size, 2)
        assertEquals(p.dict["first"], "1")
        assertEquals(p.dict["second"], "2")
    }
}