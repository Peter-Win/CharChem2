package charChem.compiler.preprocessor

import charChem.compiler.parse.isIdFirstChar

// определение нового макроса
// имя, формальные параметры, тело
// если завершение @; то ничего не выводится
// если (... , то выводится @name(...

fun defMacro(ctx: PreProcCtx) {
    val p0 = ctx.pos
    val name = ctx.searchEx("(")
    if (!isIdFirstChar(name[0])) {
        ctx.error("Invalid macro name", p0)
    }
    val macro = Macros(name)

    // считывание тела макроса
    // параметры читаются вместе с телом и разбираются при каждом вызове
    // это даёт возможность включить в них параметры вышестоящего макроса
    ctx.push()
    bodyPreprocess(ctx)
    macro.body = ctx.pop()
    // анализ окончания
    val c = ctx.n()
    if (c == "(") { // Окончание с вызовом
        ctx.write("@$name$c")
    } else if (c != ";") {
        ctx.error("Invalid macros end")
    }
    globalMacros[name] = macro
}