package charChem.math

import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

public fun is0(value: Double): Boolean {
    return abs(value) < 0.001
}
public fun toa(value: Double): String {
    val s = "${Math.rint(value * 1000.0) / 1000.0}"
    return if (s.endsWith(".0")) s.substringBefore('.') else s
}
public class Point(var x: Double = 0.0, var y: Double = 0.0 ) {
    constructor(src: Point) : this(src.x, src.y) {
    }
    override fun toString(): String =
        "(${toa(x)}, ${toa(y)})"

    override fun equals(other: Any?): Boolean =
        if (other is Point) is0(x - other.x) && is0(y - other.y) else false

    operator fun plus(pt: Point): Point =
        Point(x + pt.x, y + pt.y)

    operator fun minus(pt: Point): Point =
            Point(x - pt.x, y - pt.y)

    fun mini(pt: Point): Unit {
        x = min(x, pt.x)
        y = min(y, pt.y)
    }

    fun maxi(pt: Point): Unit {
        x = max(x, pt.x)
        y = max(y, pt.y)
    }
}