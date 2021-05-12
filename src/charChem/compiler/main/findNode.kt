package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.core.ChemNode
import charChem.core.findElement

fun findNode(compiler: ChemCompiler, ref: String): ChemNode? {
    val nodes = compiler.curAgent!!.nodes
    ref.toIntOrNull()?.let { n ->
        return nodes.getOrNull(if (n < 0) n + nodes.size else n - 1)
    }
    // Возможно, метка...
    // Если была указана метка, совпадающая с обозначением элемента, то метка имеет приоритет выше
    compiler.references[ref]?.let { node ->
        return node
    }
    // если указан элемент
    findElement(ref)?.let { elem ->
        nodes.find { it.items.size == 1 && it.items[0].obj == elem } ?. let { return it }
    }
    return null
}

fun findNodeEx(compiler: ChemCompiler, ref: String, pos: Int): ChemNode =
        findNode(compiler, ref) ?: compiler.error("Invalid node reference '[ref]'", listOf(
                "ref" to ref,
                "pos" to pos,
        ))