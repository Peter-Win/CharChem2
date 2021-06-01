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
        "520" to "H3ClO",
        "531" to "O4S{R}{R1}",
        "605" to "CO3{R1}{R2}",
        "756" to "NO2{R}",
        "757" to "NO2{R}",
        "1078" to "C8H20F2O4P2",
        "1115" to "O3S{R}2",
        "1116" to "O3S{R}{R'}",
        "1284" to "CN{R}",
        "1550" to "C6H12CaNO9^-",
)

fun compilerTest() {
    println("=== start compiler test ===")
    val main = loadTable("C:\\backup\\CharChem\\21-05-28-1\\subst_main.data")
    val bruttoIndex = mutableMapOf<String, String?>()
    main.forEach {
        val substId = it[0] ?: throw Error("Invalid substance id $it")
        bruttoIndex[substId] = it[1]
    }
    val formulas = loadTable("C:\\backup\\CharChem\\21-05-28-1\\subst_formula.data")
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