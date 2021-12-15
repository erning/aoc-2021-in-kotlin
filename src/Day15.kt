import java.util.*

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map {
            it.map { c ->
                c.digitToInt()
            }
        }
    }

    fun parseInputExtend(input: List<String>): List<List<Int>> {
        val times = 5
        val h = input.size
        val w = input.first().length
        val grid = parseInput(input)
        val extended = Array(h * times) { IntArray(w * times) }

        for (y in 0 until w) {
            for (x in 0 until h) {
                for (yi in 0 until times) {
                    for (xi in 0 until times) {
                        val v = grid[y][x] + xi + yi
                        extended[y + yi * h][x + xi * w] = v % 10 + v / 10
                    }
                }
            }
        }

        return extended.map {
            it.toList()
        }.toList()
    }

    fun buildGraph(grid: List<List<Int>>): Array<List<Pair<Int, Int>>> {
        val dir = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))

        val h = grid.size
        val w = grid.first().size

        val graph = Array<MutableList<Pair<Int, Int>>>(h * w) { mutableListOf() }
        for (i in graph.indices) {
            val x = i % w
            val y = i / w
            for (d in dir) {
                val nx = x + d.first
                val ny = y + d.second
                if (nx < 0 || nx >= w || ny < 0 || ny >= h) {
                    continue
                }
                val ni = nx + ny * w
                graph[i].add(ni to grid[ny][nx])
            }
        }
        return graph.map { it.toList() }.toTypedArray()
    }

    fun dijkstra(graph: Array<List<Pair<Int, Int>>>): Int {
        val visited: MutableSet<Int> = mutableSetOf(0)
        val queue: PriorityQueue<Pair<Int, Int>> = PriorityQueue(compareBy { it.second })
        queue.add(Pair(0, 0))

        while (true) {
            val (nodeID, nodeRisk) = queue.remove() ?: break
            if (nodeID == graph.size - 1) {
                return nodeRisk
            }
            for ((nextID, nextRisk) in graph[nodeID]) {
                if (visited.contains(nextID)) {
                    continue
                }
                val nextNode = Pair(nextID, nodeRisk + nextRisk)
                queue.add(nextNode)
                visited.add(nextID)
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        val grid = parseInput(input)
        val graph = buildGraph(grid)
        return dijkstra(graph)
    }

    fun part2(input: List<String>): Int {
        val grid = parseInputExtend(input)
        val graph = buildGraph(grid)
        return dijkstra(graph)
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
