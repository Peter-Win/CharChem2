package charChem.compiler.preprocessor

import charChem.compiler.parse.isId

// Хотя names по составу является тем же списком, который можно получить из dict.keys,
// но необходимо гарантировать точный порядок.
// Например в JavaScript такой порядок сохраняется, а C++ или Python - нет
// Поэтому список формальных параметров имеет словарь имя/значение и список имен
data class MacroParams(val dict: MutableMap<String, String>, val names: List<String>)

// Формальные параметры x[:XX]
fun readFormalPars(ctx: PreProcCtx): MacroParams {
    val dict = mutableMapOf<String, String>()
    val names = mutableListOf<String>()
    if (ctx.n() != ")") {
        ctx.pos--
        while (true) {
            val p0 = ctx.pos
            val p1 = scanPar(ctx.src, p0)

            if (p1 >= ctx.src.length) {
                ctx.error("Formal params list is not closed")
            }
            val param = ctx.n(p1 - p0) // Получено объявление очередного параметра
            val k = param.indexOf(':')
            val pair = if (k < 0) {
                // без значения по умолчанию
                Pair(param, "")
            } else {
                Pair(param.substring(0, k), param.substring(k + 1))
            }
            // Контролируем правильность описания названия параметра
            if (!isId(pair.first)) {
                ctx.error("Invalid parameter name: [name]", listOf("name" to pair.first))
            }
            dict[pair.first] = pair.second
            names.add(pair.first)
            val c = ctx.n()
            if (c == ")") break
        }
    }
    return MacroParams(dict, names)
}