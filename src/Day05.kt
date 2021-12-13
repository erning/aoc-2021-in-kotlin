import kotlin.math.abs
import kotlin.math.max

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line ->
            line.split(Regex("[\\->,\\s]+")).map {
                it.toInt()
            }
        }
    }

    fun getPoints(lines: List<List<Int>>): Map<Pair<Int, Int>, Int> {
        val points: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        for ((x1, y1, x2, y2) in lines) {
            val dx = when {
                x1 < x2 -> 1
                x1 > x2 -> -1
                else -> 0
            }
            val dy = when {
                y1 < y2 -> 1
                y1 > y2 -> -1
                else -> 0
            }
            val length = max(abs(x1 - x2), abs(y1 - y2))
            for (i in 0..length) {
                val p = Pair(x1 + i * dx, y1 + i * dy)
                points[p] = points.getOrDefault(p, 0) + 1
            }
        }
        return points
    }

    fun part1(input: List<String>): Int {
        val lines = parseInput(input).filter { (x1, y1, x2, y2) ->
            x1 == x2 || y1 == y2
        }
        val points = getPoints(lines)
        return points.count { it.value > 1 }
    }

    fun part2(input: List<String>): Int {
        val lines = parseInput(input)
        val points = getPoints(lines)
        return points.count { it.value > 1 }
    }

    val day = 5

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 5)
    check(part2(exampleInput) == 12)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
