fun main() {
    fun part1(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length
        val grid = Array(cols) { IntArray(rows) }
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                grid[x][y] = input[y][x].digitToInt()
            }
        }
        val rate = grid.map {
            it.count { v -> v == 1 }
        }.map {
            if (it > rows / 2) Pair(1, 0) else Pair(0, 1)
        }.reduce { acc, s ->
            Pair(
                acc.first.shl(1).or(s.first),
                acc.second.shl(1).or(s.second),
            )
        }
        return rate.first * rate.second
    }

    fun part2(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length
        val grid = Array(cols) { IntArray(rows) }
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                grid[x][y] = input[y][x].digitToInt()
            }
        }
        return 0
    }

    val day = 3

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 198)
//    check(part2(exampleInput) == 230)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
