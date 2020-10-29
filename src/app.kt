import charChem.math.Point
import kotlin.io.println
fun main(args: Array<String>) {
    println("Hello, Kotlin!");
    println("Arguments count: ${args.size}");
    val p0 = Point(11.0, 22.0)
    val p1 = p0 + Point(0.0, -1.0)
    println("p0 = $p0, p1=$p1")
}