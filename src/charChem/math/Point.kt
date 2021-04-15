package charChem.math

import kotlin.math.*

fun is0(value: Double): Boolean {
    return abs(value) < 0.001
}

fun toa(value: Double): String {
    val s = "${Math.rint(value * 1000.0) / 1000.0}"
    return if (s.endsWith(".0")) s.substringBefore('.') else s
}

fun pointFromRad(angle: Double): Point =
        Point(cos(angle), sin(angle))

fun pointFromDeg(angle: Double): Point =
        pointFromRad(Math.PI * angle / 180.0)

class Point(var x: Double = 0.0, var y: Double = 0.0) {
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

    operator fun times(k: Double): Point =
            Point(k * x, k * y)

    fun mini(pt: Point) {
        x = min(x, pt.x)
        y = min(y, pt.y)
    }

    fun isZero(): Boolean = is0(x) && is0(y)

    fun maxi(pt: Point) {
        x = max(x, pt.x)
        y = max(y, pt.y)
    }

    fun polarAngle(): Double {
        if (x==0.0 && y==0.0)
            return 0.0
        if (x==0.0)
            return if (y > 0.0) Math.PI/2.0 else Math.PI*3.0/2.0
        return atan2(y, x)
    }
    fun polarAngleDeg() = polarAngle() * 180.0 / Math.PI
}