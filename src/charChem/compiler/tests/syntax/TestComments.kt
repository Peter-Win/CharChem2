package charChem.compiler.tests.syntax

import charChem.compiler.ChemCompiler
import charChem.core.*
import charChem.lang.Lang
import org.testng.annotations.Test
import kotlin.test.*

class TestComments {
    @Test
    fun testPreAgent() {
        // Comment before agent
        val expr = ChemCompiler("\"Hello\"Li").exec()
        assertEquals(expr.getMessage(), "")
        val nodes: MutableList<ChemNode> = mutableListOf()
        expr.walk(object : Visitor() {
            override fun nodePre(obj: ChemNode) {
                nodes.add(obj)
            }
        })
        assertEquals(nodes.size, 1)
        assertEquals(nodes[0].items.size, 2)
        val com = nodes[0].items[0]
        assertTrue(com.obj is ChemComment)
        assertEquals(com.obj.text, "Hello")
        val el = nodes[0].items[1]
        assertTrue(el.obj is ChemAtom)
        assertEquals(el.obj.id, "Li")
    }

    @Test
    fun testCommOp() {
        // Комментарии для операции
        //   20°
        //   -->
        //   -Δ
        val expr = ChemCompiler("\"20^oC\"-->\"-[Delta]\"").exec()
        Lang.curLang = "ru"
        assertEquals(expr.getMessage(), "")
        assertEquals(expr.entities.size, 1)
        val op = expr.entities[0]
        assertTrue(op is ChemOp)
        assertEquals(op.srcText, "-->")
        val pre = op.commentPre
        assertNotNull(pre)
        assertEquals(pre.text, "20°C")
        val post = op.commentPost
        assertNotNull(post)
        assertEquals(post.text, "-Δ")

        assertEquals(pre.getBoundsBegin(), 0)
        assertEquals(pre.getBoundsEnd(), 7)
        assertEquals(post.getBoundsBegin(), 10)
        assertEquals(post.getBoundsEnd(), 20)
        assertEquals(op.getBoundsBegin(), 0)
        assertEquals(op.getBoundsEnd(), 20)
    }
}