import java.util.*

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { it.map { c -> c.digitToInt() } }
    }

    fun dijkstra(grid: List<List<Int>>, times: Int = 1): Int {
        val h = grid.size
        val w = grid.first().size
        val rows = h * times
        val cols = w * times

        val directions = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf(Pair(0, 0))
        val queue: PriorityQueue<Triple<Int, Int, Int>> = PriorityQueue(compareBy { it.third })
        queue.add(Triple(0, 0, 0))

        while (true) {
            val (x, y, costs) = queue.remove() ?: return Int.MAX_VALUE
            if (x == cols - 1 && y == rows - 1) return costs
            directions
                .map { Pair(x + it.first, y + it.second) }
                .filter { (x, y) -> x in 0 until cols && y in 0 until rows }
                .filter { !visited.contains(it) }
                .forEach { (x, y) ->
                    var cost = grid[y % h][x % w] + y / h + x / w
                    while (cost > 9) cost -= 9
                    queue.add(Triple(x, y, costs + cost))
                    visited.add(Pair(x, y))
                }
        }
    }

    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        return dijkstra(grid)
    }

    fun part2(input: List<String>): Int {
        val grid = parseInput(input)
        return dijkstra(grid, 5)
    }

    val day = 15
    val title = "Chiton"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 40)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 315)
    print("Part Two: "); println(part2(input))
}
