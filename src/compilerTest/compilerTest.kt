package compilerTest

import charChem.compiler.compile
import charChem.inspectors.makeBrutto
import charChem.inspectors.makeTextFormula
import charChem.textRules.rulesCharChem

// В тестовых целях производится проверка всех формул, которые есть в БД сайта charchem.org

val bruttoExceptions = mapOf(
        "30" to "C2H3O2{R}",
        "222" to "C9H11N2O4S{R}",
        "226" to "C2H4NO2{R}",
        "475" to "C10H12N2O8{M}^4-",
        "479" to "C10H12N2O8{M}^2-",
)

fun compilerTest() {
    println("=== start compiler test ===")
    val main = loadTable("C:\\backup\\CharChem\\21-03-16-1\\subst_main.data")
    val bruttoIndex = mutableMapOf<String, String?>()
    main.forEach {
        val substId = it[0] ?: throw Error("Invalid substance id $it")
        bruttoIndex[substId] = it[1]
    }
    val formulas = loadTable("C:\\backup\\CharChem\\21-03-16-1\\subst_formula.data")
    println("Loaded formulas = ${formulas.size}")
    formulas.forEach{ row ->
        val formulaId = row[0] ?: throw Error("Invalid formula id in $row")
        val substId = row[1] ?: throw Error("Invalid substId in $row")
        val code = row[2] ?: throw Error("Invalid formula in $row")
        val brutto = bruttoExceptions[formulaId] ?: bruttoIndex[substId] ?: throw Error("Wrong brutto in $row")
        val expr = compile(code)
        if (!expr.isOk()) {
            println("************")
            println(row)
            throw Error(expr.getMessage("ru"))
        }
        val newBrutto = makeTextFormula(makeBrutto(expr), rulesCharChem)
        if (brutto != newBrutto) {
            throw Error("need brutto: $brutto, found brutto: $newBrutto in $row")
        }
    }
    println("Success!")
}