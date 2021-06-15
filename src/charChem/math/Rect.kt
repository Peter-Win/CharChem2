package charChem.math

class Rect {
    var a: Point
    var b: Point

    constructor() {
        a = Point()
        b = Point()
    }

    constructor(rect: Rect) {
        a = Point(rect.a)
        b = Point(rect.b)
    }

    constructor(leftPos: Double, topPos: Double, rightPos: Double, bottomPos: Double) {
        a = Point(leftPos, topPos)
        b = Point(rightPos, bottomPos)
    }

    constructor(leftTop: Point, rightBottom: Point) {
        a = Point(leftTop)
        b = Point(rightBottom)
    }

    var left: Double
        get() = a.x
        set(value) {
            a.x = value
        }
    var top: Double
        get() = a.y
        set(value) {
            a.y = value
        }
    var right: Double
        get() = b.x
        set(value) {
            b.x = value
        }
    var bottom: Double
        get() = b.y
        set(value) {
            b.y = value
        }
    val width: Double
        get() = b.x - a.x
    val height: Double
        get() = b.y - a.y
    val cx: Double
        get() = (a.x + b.x) * 0.5
    val cy: Double
        get() = (a.y + b.y) * 0.5
    val center: Point
        get() = (a + b) * 0.5
    val size: Point
        get() = b - a

    fun isEmpty() = a == b
    override fun equals(other: Any?): Boolean =
            if (other is Rect) a == other.a && b == other.b else false


    fun move(delta: Point) {
        a += delta
        b += delta
    }
    fun move(x: Double, y: Double) {
        a.add(x, y)
        b.add(x, y)
    }
}