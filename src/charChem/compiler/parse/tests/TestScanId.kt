package charChem.compiler.parse.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.parse.scanId
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestScanId {
    @Test
    fun testGood() {
        val compiler = createTestCompiler("A,Bc,DE,fg,h,i1,j23,k1L2")
        assertEquals(scanId(compiler), "A")
        compiler.pos++
        assertEquals(scanId(compiler), "Bc")
        compiler.pos++
        assertEquals(scanId(compiler), "DE")
        compiler.pos++
        assertEquals(scanId(compiler), "fg")
        compiler.pos++
        assertEquals(scanId(compiler), "h")
        compiler.pos++
        assertEquals(scanId(compiler), "i1")
        compiler.pos++
        assertEquals(scanId(compiler), "j23")
        compiler.pos++
        assertEquals(scanId(compiler), "k1L2")
    }
    @Test
    fun testFail() {
        val compiler = createTestCompiler("-1*Ж_/")
        assertEquals(compiler.curChar(), '-')
        assertNull(scanId(compiler))
        compiler.pos++

        assertEquals(compiler.curChar(), '1')
        assertNull(scanId(compiler))
        compiler.pos++

        assertEquals(compiler.curChar(), '*')
        assertNull(scanId(compiler))
        compiler.pos++

        assertEquals(compiler.curChar(), 'Ж')
        assertNull(scanId(compiler))
        compiler.pos++

        assertEquals(compiler.curChar(), '_')
        assertNull(scanId(compiler))
        compiler.pos++

        assertEquals(compiler.curChar(), '/')
        assertNull(scanId(compiler))
        compiler.pos++
    }
}