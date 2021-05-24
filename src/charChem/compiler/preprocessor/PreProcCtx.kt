package charChem.compiler.preprocessor

import charChem.core.ChemError
import charChem.lang.LangParams

class PreProcCtx {
    var src: String = ""
    var dst: String = ""
    var pos: Int = 0
    val stack: MutableList<String> = mutableListOf()

    constructor(ctx: PreProcCtx) {
        src = ctx.src
        pos = ctx.pos
    }
    constructor(aSrc: String, aPos: Int) {
        src = aSrc
        pos = aPos
    }
    constructor(aSrc: String) {
        src = aSrc
    }
    fun error(msg: String, errPos: Int = 0): Nothing {
        if (errPos != 0) {
            pos = if (errPos < 0) pos + errPos else errPos
        }
        throw ChemError(msg, listOf("pos" to pos))
    }
    fun error(msg: String, params: LangParams): Nothing {
        throw ChemError(msg, params)
    }
    // считать указанное число символов
    fun n(count: Int = 1): String {
        if (count == 0) {
            return ""
        }
        if (pos + count > src.length) {
            error("Unexpected end of macros")
        }
        val start = pos
        pos += count
        return src.substring(start, pos)
    }
    // поиск подстроки
    fun search(cond: String): String? {
        val start = pos
        val stop = src.indexOf(cond, start)
        if (stop < 0) {
            return null
        }
        pos = stop + cond.length
        return src.substring(start, stop)
    }
    fun searchEx(cond: String): String =
            search(cond) ?: error("Expected [cond] character in macros", listOf("cond" to cond))
    // Достигнут ли конец?
    fun end(): Boolean = pos < src.length

    // вывод в dst
    fun write(text: String) {
        dst += text
    }
    // Записать в выходной буфер остаток исходной строки (от pos до конца)
    fun writeFinish() {
        write(src.substring(pos))
        pos = src.length
    }
    fun push() {
        stack.add(0, dst)
        dst = ""
    }
    fun pop(): String {
        val tmp = dst
        dst = stack.removeAt(0)
        return tmp
    }
    fun clear() {
        dst = ""
    }
}