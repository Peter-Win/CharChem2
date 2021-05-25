package charChem.compiler.main

import charChem.compiler.ChemCompiler
import charChem.compiler.parse.parseNum
import charChem.core.ChemBond
import charChem.math.Point
import charChem.math.pointFromDeg
import charChem.math.pointFromRad
import charChem.math.rad2deg

/*
Геометрические параметры: A, a, L, P, p, x, y
Несовместимы между собой: p, P, a, A. (В порядке убывания приоритета) Зато их можно комбинировать их с x, y
L обычно использования совместно с A или a. Может быть использовано с P, хотя в этом мало пользы.
 */

data class UniBondParam(val key: Char, val value: String, val valuePos: Int)

typealias BondParams = Map<Char, UniBondParam?>

fun makeParamsDict(args: List<String>, argPos: List<Int>): BondParams {
    val paramsList: List<UniBondParam> = args.mapIndexed { index, descr ->
        if (descr.isEmpty()) UniBondParam(' ', "", argPos[index]) else
            UniBondParam(descr[0], descr.substring(1), argPos[index] + 1)
    }.filter { it.key != ' ' }
    return paramsList.fold(mutableMapOf<Char, UniBondParam>()) { map, param ->
        map[param.key] = param
        map
    }
}

/**
 * @param count Can be negative for counter-clock wise
 */
fun createPolygonStep(compiler: ChemCompiler, count: Int, defaultLength: Double): Point {
    return getLastBond(compiler)?.dir?.let { prevDir ->
        pointFromRad(prevDir.polarAngle() + Math.PI * 2 / count) * prevDir.length()
    } ?: Point(defaultLength, 0.0)
}

fun parseRefsList(compiler: ChemCompiler, value: String, pos: Int): Point {
    if (value.isEmpty()) {
        compiler.error("Invalid node reference '[ref]'", listOf(
                "ref" to "",
                "pos" to pos
        ))
    }
    var paramPos = pos
    val points = value.split(';').map { ref ->
        val node = findNodeEx(compiler, ref, paramPos)
        paramPos += ref.length + 1
        node.pt
    }
    val vectorSum = points.reduce { sum, vec -> sum + vec }
    val midPt = vectorSum * (1.0 / points.size.toDouble())
    // Здесь используется допущение, что перед любой универсальной связью проверяется наличие узла
    return midPt - compiler.curNode!!.pt
}

fun parseAxisCoordinate(compiler: ChemCompiler, isX: Boolean, value: String, pos: Int): Double {
    return if (!value.startsWith('#')) parseNum(compiler, value, pos)
    else {
        val center = parseRefsList(compiler, value.substring(1), pos + 1)
        if (isX) center.x else center.y
    }
}

fun calcBondDirection(compiler: ChemCompiler, params: BondParams): Point {

    fun getLength(): Double = params['L']?.let {
        parseNum(compiler, it.value, it.valuePos)
    } ?: compiler.varLength

    fun fromAngle(a: Double): Point = pointFromDeg(a) * getLength()

    fun getPrevBond(): ChemBond? = getLastBond(compiler)

    val dir: Point = params['p']?.let {
        parseRefsList(compiler, it.value, it.valuePos)
    } ?: params['P']?.let {
        // Polygonal bond
        val n = if (it.value == "") 5 else parseNum(compiler, it.value, it.valuePos).toInt()
        createPolygonStep(compiler, if (n == 0) 5 else n, getLength())
    } ?: params['a']?.let { aParam ->
        val a = getPrevBond()?.dir?.let { rad2deg(it.polarAngle()) } ?: 0.0
        fromAngle(a + parseNum(compiler, aParam.value, aParam.valuePos))
    } ?: params['A']?.let {
        fromAngle(parseNum(compiler, it.value, it.valuePos))
    } ?: Point()

    params['x']?.let {
        dir.x += parseAxisCoordinate(compiler, true, it.value, it.valuePos)
    }
    params['y']?.let {
        dir.y += parseAxisCoordinate(compiler, false, it.value, it.valuePos)
    }
    return dir
}

fun parseBondMultiplicity(compiler: ChemCompiler, bond: ChemBond, param: UniBondParam) {
    val value = param.value
    fun getMode(): Char = value[1].toLowerCase()
    if (value.length == 2 && value[0] == '2' && getMode() in setOf('x', 'l', 'r', 'm')) {
        if (getMode() == 'x') {
            bond.setCross()
        } else {
            bond.align = getMode()
        }
        bond.n = 2.0
    } else {
        bond.n = parseNum(compiler, value, param.valuePos)
    }
}

fun parseStyle(bond: ChemBond, value: String) {
    if (value.isNotEmpty() && value[value.length-1].toLowerCase() in setOf('m', 'l', 'r')) {
        bond.align = value[value.length-1].toLowerCase()
        bond.style = value.substring(0, value.length - 1)
    } else {
        bond.style = value
    }
}

fun setBondProperties(compiler: ChemCompiler, bond: ChemBond, params: BondParams) {
    params['N']?.let { parseBondMultiplicity(compiler, bond, it) }
    params['h']?.let { bond.soft = true }
    params['T']?.let { bond.tx = it.value }
    if (bond.n == 1.0 && 'H' in params) bond.setHydrogen()
    params['C']?.let { // Сoordination chemical bond
        when (it.value) {
            "-" -> bond.arr0 = true     // _(C-)   A<---B
            "+" -> {                    // _(C+)   A<-->B
                bond.arr0 = true
                bond.arr1 = true
            }
            else -> bond.arr1 = true    // _(C)   A--->B
        }
    }
    params['<']?.let { bond.arr0 = true }
    params['>']?.let { bond.arr1 = true }
    params['~']?.let { bond.style = "~" }
    params['S']?.let { parseStyle(bond, it.value) }

    fun setWidth(id: String, sign: Int, isGlobal: Boolean) {
        val step: Pair<Int?, Int?> = when (id) {
            "+" -> Pair(0, sign)
            "-" -> Pair(sign, 0)
            "0", "1" -> Pair(0, 0)
            "2" -> Pair(sign, sign)
            else -> Pair(null, null)
        }
        bond.w0 = step.first ?: compiler.curWidth
        bond.w1 = step.second ?: compiler.curWidth
        if (isGlobal) {
            compiler.curWidth = step.second ?: compiler.curWidth
        }
    }
    params['w']?.let { setWidth(it.value,1, false) }
            ?: params['d']?.let { setWidth(it.value,-1, false) }
            ?: params['W']?.let { setWidth(it.value, 1, true) }
            ?: params['D']?.let { setWidth(it.value, -1, true) }
            ?: setWidth("", 0, false)
}

fun createUniversalBond(compiler: ChemCompiler, args: List<String>, argPos: List<Int>) {
    val bond = createCommonBond(compiler)
    if (compiler.curNode == null) {
        println("createUniversalBond > openNode")
        // openNode(compiler, true)
        getNodeForBondStart(compiler, bond)
    }
    val params = makeParamsDict(args, argPos)
    bond.dir = calcBondDirection(compiler, params)
    setBondProperties(compiler, bond, params)
    onOpenBond(compiler, bond)
}