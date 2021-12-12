import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input[0].split(",").map {
            it.toInt()
        }
        val maxPosition = positions.maxOrNull()!!
        var minFuel = Int.MAX_VALUE
        for (i in 0..maxPosition) {
            val fuel = positions.sumOf {
                abs(it - i)
            }
            if (fuel < minFuel) {
                minFuel = fuel
            }
        }
        return minFuel
    }

    fun part2(input: List<String>): Int {
        val positions = input[0].split(",").map {
            it.toInt()
        }
        val maxPosition = positions.maxOrNull()!!
        var minFuel = Int.MAX_VALUE
        for (i in 0..maxPosition) {
            val fuel = positions.sumOf {
                val n = abs(it - i)
                n * (n + 1) / 2
            }
            if (fuel < minFuel) {
                minFuel = fuel
            }
        }
        return minFuel
    }

    val day = 7

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 37)
    check(part2(exampleInput) == 168)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
