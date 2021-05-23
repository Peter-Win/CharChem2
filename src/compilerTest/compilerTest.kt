package compilerTest

import charChem.compiler.compile

// В тестовых целях производится проверка всех формул, которые есть в БД сайта charchem.org

fun compilerTest() {
    println("=== start compiler test ===")
    val formulas = loadTable("C:\\backup\\CharChem\\21-03-16-1\\subst_formula.data")
    println("Loaded formulas = ${formulas.size}")
    formulas.forEach{ row ->
        val code = row[2]
        val expr = compile(code)
        if (!expr.isOk()) {
            println("************")
            println(row)
            throw Error(expr.getMessage("ru"))
        }
    }
}