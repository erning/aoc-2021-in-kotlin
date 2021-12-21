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
            var position = (positions[player] - 1 + step) % 10 + 1
            val score = scores[player] + position
            positions[player] = position
            scores[player] = score

            player = (player + 1) % 2
            if (score >= 1000) break
        }

        return times * 3 * scores[player]
    }

    fun part2(input: List<String>): Long {
        val positions = parseInput(input)

        val dices: List<List<Int>> = fun(): List<List<Int>> {
            val dices = mutableListOf<List<Int>>()
            for (a in 1..3) {
                for (b in 1..3) {
                    for (c in 1..3) {
                        dices.add(listOf(a, b, c))
                    }
                }
            }
            return dices
        }()
        val cache: MutableMap<String, List<Long>> = mutableMapOf()
        
        fun play(player: Int, status: List<Pair<Int, Int>>): List<Long> {
            val winners: MutableList<Long> = mutableListOf(0L, 0L)
            for (dice in dices) {
                val step = dice.sum()
                val position = (status[player].first - 1 + step) % 10 + 1
                val score = status[player].second + position

                if (score >= 21) {
                    winners[player] += 1L
                } else {
                    val nextStatus = status.toMutableList()
                    nextStatus[player] = Pair(position, score)
                    val key = "$player$nextStatus"
                    val nextWinner = cache[key] ?: play((player + 1) % 2, nextStatus)
                    cache[key] = nextWinner
                    winners[0] += nextWinner[0]
                    winners[1] += nextWinner[1]
                }
            }
            return winners
        }

        val winners = play(0, listOf(Pair(positions[0], 0), Pair(positions[1], 0)))
        return winners.maxOf { it }
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
