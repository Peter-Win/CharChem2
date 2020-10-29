package charChem.math

import kotlin.math.floor
import kotlin.math.roundToInt

fun roundMass(mass: Double): Double = floor(mass * 100.0) / 100.0

fun strMass(mass: Double): String {
    val iMass = mass.roundToInt()
    var rMass = roundMass(mass)
    return if (iMass.toDouble() == rMass) {
        iMass.toString()
    } else {
        rMass.toString()
    }
}