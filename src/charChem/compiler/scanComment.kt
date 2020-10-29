package charChem.compiler

// Извлекает комментарий
// Изначально pos должен быть установлен на первый символ внутри кавычек
// В конце он установлен на завершающую кавычку

fun scanComment(compiler: ChemCompiler): String {
    val pos0 = compiler.pos
    if (!compiler.scanTo('"'))
        compiler.error("Comment is not closed", listOf("pos" to pos0 - 1))
    return compiler.subStr(pos0)
}