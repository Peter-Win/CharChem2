package charChem.compiler.preprocessor

import charChem.compiler.parse.isId

fun applyParamValues(def: MacroParams, params: List<String>, ctx: PreProcCtx): PreProcCtx {
    if (def.names.isEmpty()) {
        return ctx
    }
    var curIndex = 0
    // Подставляем фактические значения
    params.forEach { paramValue ->
        val k = paramValue.indexOf(':')
        var ready = false
        if (k >= 0) {
            val id = paramValue.substring(0, k)
            if (id in def.dict) {
                def.dict[id] = paramValue.substring(k+1)
                ready = true
            }
        }
        if (!ready) {
            val name = def.names.getOrNull(curIndex++)
            // Индексный параметр может быть пропущен, если пуст.
            // Тогда вместо него будет использовано значение по умолчанию
            if (name != null && paramValue.isNotEmpty()) {
                def.dict[name] = paramValue
            }
        }
    }
    // Замена параметров на значения
    ctx.writeFinish()
    val exl = ctx.dst.split('&').toMutableList()
    exl.subList(1, exl.size).forEachIndexed { index, s ->
        val i = index + 1
        val id = def.names.fold("") { prev, f ->
            if (s.substring(0, f.length.coerceAtMost(s.length)) == f && f.length > prev.length) f else prev
        }
        // Если в формуле встретился знак &, с которым не связан ни один параметр, пропускаем
        if (id.isNotEmpty()) {
            // Замена параметра на значение
            exl[i] = def.dict[id] + exl[i].substring(id.length)
        }
    }
    return PreProcCtx(exl.joinToString(separator = ""))
}

fun readRealParams(ctx: PreProcCtx): List<String> {
    val result = mutableListOf<String>()
    if (ctx.n() != ")") {
        ctx.pos--
        while (true) {
            val p0 = ctx.pos
            val p1 = scanPar(ctx.src, p0)
            if (p1 >= ctx.src.length) {
                ctx.error("Real params list is not closed")
            }
            result.add(ctx.n(p1-p0))
            if (ctx.n() == ")") break
        }
    }
    return result
}

// Исполнение макроса
// params - индексный список фактических параметров, в тексте которых могут быть имена
// так сделано из-за того, что до вызова точно не известно число формальных параметров
fun execMacros(src: String, params: List<String>): String {
    val ctx0 = PreProcCtx(src)
    // Извлечение формальных параметров
    val p = readFormalPars(ctx0)
    val ctx1 = applyParamValues(p, params, ctx0)
    // Расшифровка всех макросов @A()
    while (true) {
        val c = ctx1.search("@")
        if (c == null) {
            ctx1.writeFinish()
            break
        }
        // Встречено объявление. Это может быть только конструкция @A
        ctx1.write(c)
        val name = ctx1.searchEx("(")
        if (!isId(name)) {
            ctx1.error("Invalid macro [name]", listOf("name" to name))
        }
        globalMacros[name]?.let { macro ->
            // Извлечение фактических параметров
            val realParams = readRealParams(ctx1)
            ctx1.write(execMacros(macro.body, realParams))
        } ?: ctx1.error("Macros not found: [name]", listOf("name" to name))
    }
    return ctx1.dst
}