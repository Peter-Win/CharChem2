package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.execMacros
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestExecMacros {
    @Test
    fun testSimple() {
        assertEquals(execMacros(")test", listOf()), "test")
        assertEquals(execMacros("x)[&x*&x]", listOf("123")), "[123*123]")
        assertEquals(execMacros("x:1,xx:2,xxx:3){&xxx,&xx,&x}", listOf("one")), "{3,2,one}")
    }
}