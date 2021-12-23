import java.util.*

fun main() {
    val burrowMap: List<List<Int>> = fun(): List<List<Int>> {
        val map: List<List<Int>> = listOf(
            listOf(1),          // 0
            listOf(0, 2),       // 1
            listOf(1, 3, 11),   // 2
            listOf(2, 4),       // 3
            listOf(3, 5, 13),   // 4
            listOf(4, 6),       // 5
            listOf(5, 7, 15),   // 6
            listOf(6, 8),       // 7
            listOf(7, 9, 17),   // 8
            listOf(8, 10),      // 9
            listOf(9),          // 10
            listOf(2, 12),      // 11
            listOf(11),         // 12
            listOf(4, 14),      // 13
            listOf(13),         // 14
            listOf(6, 16),      // 15
            listOf(15),         // 16
            listOf(8, 18),      // 17
            listOf(17),         // 18
        )

        // TODO: path
        fun dijkstra(start: Int): List<Int> {
            val visited: MutableMap<Int, Int> = mutableMapOf(start to 0)
            val queue: PriorityQueue<Pair<Int, Int>> = PriorityQueue(compareBy { it.second })
            queue.add(Pair(start, 0))
            while (queue.isNotEmpty()) {
                val (i, dist) = queue.remove()
                map[i]
                    .filter { !visited.contains(it) }
                    .forEach {
                        queue.add(Pair(it, dist + 1))
                        visited[it] = dist + 1
                    }
            }
            if (start <= 10) {
                (0..10).filter { start != it }.forEach { visited[it] = 0 }
            } else {
                (2..8 step 2).forEach { visited[it] = 0 }
            }
            return (0..visited.keys.maxOf { it }).map { visited[it] ?: 0 }
        }
        return (0..18).map { dijkstra(it) }
    }()

    val energyCostMap: Map<Char, Int> = mapOf(
        'A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000
    )

    fun moveAmphipod(amphipods: Map<Int, Char>, from: Int, to: Int): Map<Int, Char>? {
        val amphipod = amphipods[from] ?: return null
        if (amphipods.containsKey(to)) return null
        val new = amphipods.toMutableMap()
        // TODO: check movement rules!
        new.remove(from)
        new[to] = amphipod
        return new
    }

    fun parseInput(input: List<String>): List<Int> {
        return listOf()
    }

    fun printAmphipods(m: Map<Int, Char>) {
        println("+-----------------------+")
        print("|")
        (0..10).forEach { print(" "); print(m[it] ?: '.') }
        println(" |")
        print("+---")
        (11..17 step 2).forEach {
            print("|"); print(" "); print(m[it] ?: '.'); print(" ")
        }
        println("|---+")
        print("   ")
        (12..18 step 2).forEach {
            print(" |"); print(" "); print(m[it] ?: '.');
        }
        println(" |")
        println("    +---------------+")
    }

    fun isDone(amphipods: Map<Int, Char>): Boolean {
        val final = mapOf(
            11 to 'A', 13 to 'B', 15 to 'C', 17 to 'D',
            12 to 'A', 14 to 'B', 16 to 'C', 18 to 'D',
        )
        amphipods.forEach { (position, name) ->
            if (final[position] != name) return false
        }
        return true
    }

    fun next(amphipods: Map<Int, Char>): List<Map<Int, Char>> {
        val rv: MutableList<Map<Int, Char>> = mutableListOf()
        for ((position, name) in amphipods) {
            val nextPostions = burrowMap[position].indices.filter { burrowMap[position][it] > 0 }
            for (nextPosition in nextPostions) {
                val new = moveAmphipod(amphipods, position, nextPosition) ?: continue
                printAmphipods(new)
                println()
            }
        }
        return rv.toList()
    }

    fun part1(input: List<String>): Int {
        val amphipods: Map<Int, Char> = mapOf(
            11 to 'B', 13 to 'C', 15 to 'B', 17 to 'D',
            12 to 'A', 14 to 'D', 16 to 'C', 18 to 'A',
        )
        println(isDone(amphipods))
        burrowMap.forEach {
            println(it)
        }
        printAmphipods(amphipods)
        println()
        next(amphipods).forEach {
            printAmphipods(it)
            println()
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val day = 23
    val title = "Amphipod"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 12521)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == -1)
    print("Part Two: "); println(part2(input))
}
