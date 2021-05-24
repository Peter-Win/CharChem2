package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.MacroParams
import charChem.compiler.preprocessor.PreProcCtx
import charChem.compiler.preprocessor.applyParamValues
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestApplyParamValues {
    @Test
    fun testUseRealParams() {
        val def = MacroParams(mutableMapOf("x" to "xDef", "y" to "yDef"), listOf("x", "y"))
        val params = listOf("xReal", "yReal")
        val ctx0 = PreProcCtx("(&x+&y)")
        val ctx1 = applyParamValues(def, params, ctx0)
        assertEquals(ctx1.src, "(xReal+yReal)")
    }
    @Test
    fun testUseDefaultParams() {
        val def = MacroParams(mutableMapOf("x" to "xDef", "y" to "yDef"), listOf("x", "y"))
        val params = listOf<String>()
        val ctx0 = PreProcCtx("(&x+&y)")
        val ctx1 = applyParamValues(def, params, ctx0)
        assertEquals(ctx1.src, "(xDef+yDef)")
    }
    @Test
    fun testUseSimilarNames() {
        val def = MacroParams(mutableMapOf("x" to "1", "xx" to "2", "xxx" to "3"), listOf("x", "xx", "xxx"))
        val params = listOf("one")
        val ctx0 = PreProcCtx("[&xxx, &xx, &x]")
        val ctx1 = applyParamValues(def, params, ctx0)
        assertEquals(ctx1.src, "[3, 2, one]")
    }
}