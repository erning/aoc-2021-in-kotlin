fun main() {
    val neighbors = listOf(
        Pair(-1, -1), Pair(0, -1), Pair(1, -1),
        Pair(-1, 0), Pair(1, 0),
        Pair(-1, 1), Pair(0, 1), Pair(1, 1)
    )

    fun increaseEnergyLevel(grid: MutableList<MutableList<Int>>, cols: Int, rows: Int) {
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                grid[y][x] += 1
            }
        }
    }

    fun flash(grid: MutableList<MutableList<Int>>, cols: Int, rows: Int): Int {
        var count = 0
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                if (grid[y][x] > 9) {
                    count += 1
                    for ((dx, dy) in neighbors) {
                        val nx = x + dx
                        val ny = y + dy
                        if (nx in 0 until cols && ny in 0 until rows && grid[ny][nx] > 0) {
                            grid[ny][nx] += 1
                        }
                    }
                    grid[y][x] = 0
                }
            }
        }
        return count
    }

    fun part1(input: List<String>): Int {
        val grid = input.map {
            it.toCharArray().map { c ->
                c.digitToInt()
            }.toMutableList()
        }.toMutableList()
        val rows = grid.size
        val cols = grid[0].size
        var count = 0
        for (step in 1..100) {
            increaseEnergyLevel(grid, cols, rows)
            do {
                val c = flash(grid, cols, rows)
                count += c
            } while (c > 0)
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val grid = input.map {
            it.toCharArray().map { c ->
                c.digitToInt()
            }.toMutableList()
        }.toMutableList()
        val rows = grid.size
        val cols = grid[0].size
        var step = 0
        do {
            step += 1
            increaseEnergyLevel(grid, cols, rows)
            var count = 0
            do {
                val c = flash(grid, cols, rows)
                count += c
            } while (c > 0)
        } while (count < rows * cols)
        return step
    }

    val day = 11

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 1656)
    check(part2(exampleInput) == 195)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
