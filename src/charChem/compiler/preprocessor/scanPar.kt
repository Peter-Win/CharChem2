package charChem.compiler.preprocessor

// Определение границы параметра. Ограничителем является знак , или )
fun scanPar(src: String, start: Int): Int {
    // нужно учитывать баланс скобок и кавычек
    var lock = 0
    var isComment = false
    var pos = start
    while (pos < src.length) {
        val c = src[pos]
        if (c == '"') isComment = !isComment
        else if (c=='(' && !isComment) lock++
        else if (c==',' && !isComment && lock == 0) break
        else if (c==')' && !isComment) {
            if (lock>0) lock--
            else break
        }
        pos++
    }
    return pos
}