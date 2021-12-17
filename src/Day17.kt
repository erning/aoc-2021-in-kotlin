fun main() {
    fun parseInput(input: List<String>): List<Int> {
        return input
            .first().split(":").last()
            .split(",").map { it.split("=").last() }
            .map { it.split("..").map { n -> n.toInt() } }
            .reduce { acc, it -> acc + it }
    }

    data class Probe(var x: Int, var y: Int, var vx: Int, var vy: Int) {
        fun next(): Probe {
            return Probe(x + vx, y + vy, if (vx > 1) vx - 1 else 0, vy - 1)
        }
    }

    fun part1(input: List<String>): Int {
        val (_, _, y1, y2) = parseInput(input)
        for (i in y1 until 0) {
            var probe = Probe(0, 0, 0, -i)
            while (probe.y > y2) {
                probe = probe.next()
            }
            if (probe.y < y1) {
                continue
            }
            return i * (i - 1) / 2
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val (x1, x2, y1, y2) = parseInput(input)
        var count = 0
        for (vx in 0..x2) {
            for (vy in y1..-y1) {
                var probe = Probe(0, 0, vx, vy)
                while (probe.x < x1 && probe.y > y2) {
                    probe = probe.next()
                }
                while (probe.x <= x2 && probe.y >= y1) {
                    if (probe.x in x1..x2 && probe.y in y1..y2) {
                        count++
                        break
                    }
                    probe = probe.next()
                }
            }
        }
        return count
    }

    val day = 17
    val title = "Trick Shot"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 45)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 112)
    print("Part Two: "); println(part2(input))
}
