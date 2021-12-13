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
                    count++
                    neighbors.map {
                        Pair(x + it.first, y + it.second)
                    }.filter {
                        (it.first in 0 until cols) && (it.second in 0 until rows)
                    }.filter {
                        grid[it.second][it.first] > 0
                    }.forEach {
                        grid[it.second][it.first] += 1
                    }
                    grid[y][x] = 0
                }
            }
        }
        return count
    }

    fun part1(input: List<String>): Int {
        val grid = input.map {
            it.map { c ->
                c.digitToInt()
            }.toMutableList()
        }.toMutableList()
        val rows = grid.size
        val cols = grid[0].size
        var count = 0
        repeat(100) {
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
            it.map { c ->
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
