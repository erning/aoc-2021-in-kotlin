fun main() {
    data class RebootStep(val on: Boolean, val xr: IntRange, val yr: IntRange, val zr: IntRange)

    fun parseInput(input: List<String>): List<RebootStep> {
        return input.map { line ->
            line.split(" ").map {
                when (it) {
                    "on" -> listOf(true)
                    "off" -> listOf(false)
                    else -> {
                        it.split(",").map { range ->
                            range.substring(2).split("..").map { v -> v.toInt() }
                        }.map { v ->
                            IntRange(v[0], v[1])
                        }
                    }
                }
            }.reduce { acc, v -> acc + v }
        }.map {
            RebootStep(it[0] as Boolean, it[1] as IntRange, it[2] as IntRange, it[3] as IntRange)
        }
    }

    fun part1(input: List<String>): Int {
        val grid: MutableSet<Triple<Int, Int, Int>> = mutableSetOf()
        parseInput(input).forEach { step ->
            step.xr.filter { it >= -50 && it <= 50 }.forEach { x ->
                step.yr.filter { it >= -50 && it <= 50 }.forEach { y ->
                    step.zr.filter { it >= -50 && it <= 50 }.forEach { z ->
                        if (step.on) {
                            grid.add(Triple(x, y, z))
                        } else {
                            grid.remove(Triple(x, y, z))
                        }
                    }
                }
            }
        }
        return grid.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val day = 22
    val title = "Reactor Reboot"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 590784)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == -1)
    print("Part Two: "); println(part2(input))
}
