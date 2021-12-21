fun main() {
    fun parseInput(input: List<String>): IntArray {
        return listOf(
            input[0].last().digitToInt(),
            input[1].last().digitToInt(),
        ).toIntArray()
    }

    fun part1(input: List<String>): Int {
        val positions = parseInput(input)
        val scores = listOf(0, 0).toIntArray()
        var player = 0
        var times = 0

        while (true) {
            times++
            val dice = times * 3 - 1
            val step = dice * 3
            var p = positions[player]
            p = (p - 1 + step) % 10 + 1
            val s = scores[player] + p
            positions[player] = p
            scores[player] = s

            player = (player + 1) % 2
            if (s >= 1000) break
        }

        return times * 3 * scores[player]
    }

    fun part2(input: List<String>): Long {
        val positions = parseInput(input)
        return 0
    }

    val day = 21
    val title = "Dirac Dice"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 739785)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 444356092776315)
    print("Part Two: "); println(part2(input))
}
