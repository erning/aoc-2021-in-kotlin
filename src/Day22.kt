import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Segment(val a: Int, val b: Int) {
        val size: Int get() = b - a + 1

        fun intersect(other: Segment): Segment? {
            val i = max(a, other.a)
            val j = min(b, other.b)
            return if (i <= j) Segment(i, j) else null
        }
    }

    data class Cube(val x: Segment, val y: Segment, val z: Segment) {
        val size: Long get() = x.size.toLong() * y.size.toLong() * z.size.toLong()

        fun intersect(other: Cube): Cube? {
            val xi = x.intersect(other.x) ?: return null
            val yi = y.intersect(other.y) ?: return null
            val zi = z.intersect(other.z) ?: return null
            return Cube(xi, yi, zi)
        }
    }

    data class Instruction(val operator: Boolean, val operand: Cube)

    class Reactor {
        val instructions: MutableList<Instruction> = mutableListOf()

        val size: Long get() {
            var size = 0L
            for ((operator, operand) in instructions) {
                size += operand.size * if (operator) 1 else -1
            }
            return size
        }

        fun turn(isOn: Boolean, cube: Cube) {
            val additions: MutableList<Instruction> = mutableListOf()
            for ((operator, operand) in instructions) {
                val i = cube.intersect(operand) ?: continue
                additions += Instruction(!operator, i)
            }
            if (isOn) {
                instructions.add(Instruction(true, cube))
            }
            instructions.addAll(additions)
        }
    }

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.filter { it.isNotEmpty() }.map { line ->
            line.split(" ").map {
                when (it) {
                    "on" -> listOf(1)
                    "off" -> listOf(0)
                    else -> {
                        it.split(",").map { range ->
                            range.substring(2).split("..").map { v -> v.toInt() }
                        }.reduce { acc, v -> acc + v }
                    }
                }
            }.reduce { acc, v -> acc + v }
        }
    }

    fun part1(input: List<String>): Long {
        val steps = parseInput(input)
        val reactor = Reactor()
        for (step in steps) {
            if (step[1] < -50 || step[2] > 50) continue
            if (step[3] < -50 || step[4] > 50) continue
            if (step[5] < -50 || step[6] > 50) continue
            val x = Segment(step[1], step[2])
            val y = Segment(step[3], step[4])
            val z = Segment(step[5], step[6])
            val cube = Cube(x, y, z)
            reactor.turn(step[0] == 1, cube)
        }
        return reactor.size
    }

    fun part2(input: List<String>): Long {
        val steps = parseInput(input)
        val reactor = Reactor()
        for (step in steps) {
            val x = Segment(step[1], step[2])
            val y = Segment(step[3], step[4])
            val z = Segment(step[5], step[6])
            val cube = Cube(x, y, z)
            reactor.turn(step[0] == 1, cube)
        }
        return reactor.size
    }

    val day = 22
    val title = "Reactor Reboot"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 590784L)
    print("Part One: "); println(part1(input))

    val exampleInput2 = readInput(day, "example2")
    check(part2(exampleInput2) == 2758514936282235L)
    print("Part Two: "); println(part2(input))
}
