package charChem.compiler.parse.tests

import charChem.compiler.createTestCompiler
import charChem.compiler.parse.scanTo
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestScan {
    @Test
    fun testScanToFinal() {
        val compiler = createTestCompiler("A\"123\"")
        compiler.pos = 2
        val res = scanTo(compiler, '"')
        assertTrue(res)
        assertEquals(compiler.subStr(2), "123")
        assertEquals(compiler.pos, compiler.text.lastIndexOf('"'))
    }
}