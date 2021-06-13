package compilerTest

import charChem.compiler.compile
import charChem.inspectors.isAbstract
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
        "2030" to "C15H4O{R1}{R2}{R3}{R4}{R5}{R6}{R7}^+",
        "2032" to "C15H11ClO6",
        "2036" to "C17H15ClO7",
        "2038" to "C15H11ClO5",
        "2040" to "C15H11ClO5",
        "2042" to "C17H15ClO7",
        "2044" to "C16H13ClO6",
        "2047" to "C17H15ClO6",
        "2144" to "C15H3O2{R3'}{R4'}{R5}{R5'}{R6}{R7}{R8}",
        "2748" to "C288H318O99{...}3",
        "2786" to "CNO2{R1}{R2}{R3}",
        "2787" to "CHNO2{R}{R'}",
        "3226" to "{Uuq}",
        "3230" to "{Uuh}",
        "3233" to "{Uue}",
        "3297" to "N4O2",
        "3614" to "C10H7N2{R4}{R5}{RN1}{RN2}{Rα}",
        "4147" to "C18H22O3{R}{R'}",
        "4169" to "C8H3N",
        "5061" to "C7H12NO8P{R}2",
        "5062" to "C7H12NO8P{R}2",
        "5454" to "C12H30Al2",
        "6130" to "C7H6N2O3S{R}{R2}",
        "6205" to "C2H4NO2{R}",
        "6392" to "C24H39O21",
        "6393" to "C24H39O21",
        "7279" to "C8H12NO10P{R}2",
        "7281" to "C42H82NO10P",
        "7437" to "H4Fe3S4^2-",
        "7736" to "Au2Cl6",
        "8613" to "O6Sb4",
        "8616" to "S6Sb4",
        "8887" to "Cl12Mo6",
        "9299" to "Be5Cl10",
        "9310" to "Be3Br4",
        "9315" to "Be3I4",
        "9434" to "C12H24Be4O13",
        "10429" to "B3N3",
        "10430" to "B3N3",
        "13185" to "C18H21O19^3-",
        "10830" to "H3B3O6",
        "11867" to "O5P2",
        "12028" to "Cl10W2",
        "12044" to "C6H18Al2",
        "12165" to "Cl12W6",
        "12241" to "C4H12Li4",
        "12434" to "Au2Cl6",
        "12541" to "Cl3I",
        "12542" to "Cl3I",
        "12684" to "HgNO3",
        "13365" to "H2Ni2O4",
        "13421" to "Br6Fe2",
        "14563" to "H12B12",
        "14603" to "C4H8Ag2O4",
        "14868" to "BNaO3",
        "15228" to "C81H87O15S3^3-",
        "15359" to "Ag4O4",
        "15360" to "AgO",
        "15629" to "H4O9Re2",
        "15630" to "H4O9Re2",
        "15774" to "Cl9Re3",
        "16768" to "C14H30Fe3O18",
)

fun compilerTest() {
    println("=== start compiler test ===")
    val main = loadTable("C:\\backup\\CharChem\\21-06-13-1\\subst_main.data")
    val bruttoIndex = mutableMapOf<String, String?>()
    main.forEach {
        val substId = it[0] ?: throw Error("Invalid substance id $it")
        bruttoIndex[substId] = it[1]
    }
    val formulas = loadTable("C:\\backup\\CharChem\\21-06-13-1\\subst_formula.data")
    println("Loaded formulas = ${formulas.size}")
    formulas.forEach{ row ->
        val formulaId = row[0] ?: throw Error("Invalid formula id in $row")
        val substId = row[1] ?: throw Error("Invalid substId in $row")
        val code = row[2] ?: throw Error("Invalid formula in $row")
        val expr = compile(code)
        if (!expr.isOk()) {
            println("************")
            println(row)
            throw Error(expr.getMessage("ru"))
        }
        if (!isAbstract(expr)) {
            val brutto = bruttoExceptions[formulaId] ?: bruttoIndex[substId] ?: throw Error("Wrong brutto in $row")
            val newBrutto = makeTextFormula(makeBrutto(expr), rulesCharChem)
            if (brutto != newBrutto) {
                throw Error("need brutto: $brutto, found brutto: $newBrutto in $row")
            }
        }
    }
    println("Success!")
}