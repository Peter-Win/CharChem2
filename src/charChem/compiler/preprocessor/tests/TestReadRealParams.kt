package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.PreProcCtx
import charChem.compiler.preprocessor.readRealParams
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestReadRealParams {
    @Test
    fun testEmpty() {
        assertEquals(readRealParams(PreProcCtx("A()", 2)), listOf())
    }
    @Test
    fun testSingle() {
        assertEquals(readRealParams(PreProcCtx("A(abc)", 2)), listOf("abc"))
    }
    @Test
    fun testWithSkip() {
        assertEquals(readRealParams(PreProcCtx("A(x,,z)", 2)), listOf("x", "", "z"))
    }
    @Test
    fun testNested() {
        assertEquals(readRealParams(PreProcCtx("A(rgb(255,0,0),\"hello, world!\")", 2)),
                listOf("rgb(255,0,0)", "\"hello, world!\""))
    }
}