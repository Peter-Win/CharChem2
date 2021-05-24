package charChem.compiler.preprocessor

fun mainPreProcess(src: String): String {
    // основной алгоритм выполнения препроцессора для заданной строки
    val ctx = PreProcCtx(src)
    bodyPreprocess(ctx)
    if (ctx.pos != src.length) {
        ctx.error("Invalid preprocessor finish")
    }
    val dummyBody = ")" + ctx.dst
    return execMacros(dummyBody, listOf())
}