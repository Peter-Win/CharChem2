package charChem.compiler.preprocessor.tests

import charChem.compiler.preprocessor.PreProcCtx
import charChem.compiler.preprocessor.bodyPreprocess
import charChem.compiler.preprocessor.globalMacros
import org.testng.annotations.Test
import kotlin.test.assertEquals

class TestBodyPreprocess {
    @Test
    fun testWithoutMacro() {
        val ctx = PreProcCtx("H2SO4")
        bodyPreprocess(ctx)
        assertEquals(ctx.dst, "H2SO4")
    }
    @Test
    fun testSingleMacroUsing() {
        val ctx = PreProcCtx("a+@point(x,1)")
        bodyPreprocess(ctx)
        assertEquals(ctx.dst, "a+@point(x,1)")
    }
    @Test
    fun testEndBySemicolon() {
        val ctx = PreProcCtx("@:A()first+@second(x,y)@;next", 5)
        bodyPreprocess(ctx)
        assertEquals(ctx.dst, "first+@second(x,y)")
    }
    @Test
    fun testEndByExec() {
        val ctx = PreProcCtx("@:A(x,y)@B(&x)+&y@(1,2)", 8)
        bodyPreprocess(ctx)
        assertEquals(ctx.dst, "@B(&x)+&y")
    }
    @Test
    fun testNestedMacroDefinition() {
        val ctx = PreProcCtx("@:A(x,y)[@:B(p)(&p*&p)@(&x)+@B(&y)]@(2,3)", 8)
        bodyPreprocess(ctx)
        assertEquals(ctx.dst, "[@B(&x)+@B(&y)]")
        assertEquals(globalMacros["B"]?.body, "p)(&p*&p)")
    }
}