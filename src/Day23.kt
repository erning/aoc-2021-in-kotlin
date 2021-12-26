import java.util.*

fun main() {
    class Rule(val depth: Int = 2) {
        val doors = listOf(2, 4, 6, 8)
        val hallway = (0..10).toList().filter { it !in doors }
        val rooms: List<List<Int>> by lazy {
            (0..3).map { i ->
                (0 until depth).map { it * 4 + 11 + i }
            }
        }

        val costs: IntArray = intArrayOf(1, 10, 100, 1000)

        val paths: Map<Int, Map<Int, List<Int>>> by lazy {
            fun getPath(a: Int, b: Int): List<Int> {
                assert(b <= 10)
                if (a <= 10) {
                    return if (a < b) (a..b).toList() else (b..a).toList().reversed()
                }
                val path: MutableList<Int> = mutableListOf()
                path.add(a)
                when (a) {
                    11 -> path.addAll(getPath(2, b))
                    12 -> path.addAll(getPath(4, b))
                    13 -> path.addAll(getPath(6, b))
                    14 -> path.addAll(getPath(8, b))
                    else -> path.addAll(getPath(a - 4, b))
                }
                return path
            }

            val paths: MutableMap<Int, MutableMap<Int, List<Int>>> = mutableMapOf()
            for (i in 0..3) {
                for (p1 in rooms[i]) {
                    for (p2 in hallway) {
                        val path = getPath(p1, p2)
                        if (!paths.containsKey(p1)) {
                            paths[p1] = mutableMapOf()
                        }
                        if (!paths.containsKey(p2)) {
                            paths[p2] = mutableMapOf()
                        }
                        paths[p1]!![p2] = path
                        paths[p2]!![p1] = path.reversed()
                    }
                }
            }
            paths
        }

        val distances: Map<Int, Map<Int, Int>> by lazy {
            paths.map { from ->
                Pair(
                    from.key,
                    from.value.map { to ->
                        Pair(to.key, to.value.size - 1)
                    }.toMap()
                )
            }.toMap()
        }

        val final: List<Char> by lazy {
            var s = "..........."
            (1..depth).forEach {
                s += "ABCD"
            }
            s.toList()
        }
    }

    lateinit var rule: Rule

    data class State(val data: List<Char>) {
        override fun toString(): String = String(data.toCharArray())

        val next: List<Pair<State, Int>> by lazy {
            val children: MutableList<Pair<State, Int>> = mutableListOf()

            val srcs = (data.indices).filter { data[it] != '.' }
            val dsts = (data.indices).filter { data[it] == '.' }

            for (from in srcs) {
                if (from > 10) {
                    if (from != rule.rooms[(from - 11) % 4].find { data[it] != '.'}) continue
                }
                for (to in dsts) {
                    val path = rule.paths[from]?.get(to) ?: continue
                    if (to > 10) {
                        if ((to - 11) % 4 != data[from] - 'A') continue
                        if (rule.rooms[data[from] - 'A'].any { data[it] != '.' && data[it] != data[from] }) continue
                        if (to != rule.rooms[data[from] - 'A'].findLast { data[it] == '.' }) continue
                    }
                    if (path.drop(1).any { data[it] != '.' }) continue
                    val distance = rule.distances[from]!![to]!!
                    val cost = rule.costs[data[from] - 'A'] * distance
                    val state = move(from, to)
                    children.add(state to cost)
                }
            }

            children.sortedBy { it.second }
        }

        fun move(from: Int, to: Int): State {
            assert(data[from] != '.')
            assert(data[to] == '.')
            val chs = data.toMutableList()
            chs[to] = chs[from]
            chs[from] = '.'
            return State(chs)
        }
    }

    fun search(initial: State): Pair<Int, List<State>>? {
        val visited: MutableSet<State> = mutableSetOf()
        val queue: PriorityQueue<Triple<State, Int, List<State>>> = PriorityQueue(compareBy { it.second })
        queue.add(Triple(initial, 0, listOf()))

        while (queue.isNotEmpty()) {
            val curr = queue.remove()
            if (visited.contains(curr.first)) continue
            visited.add(curr.first)

            if (curr.first.data == rule.final) {
                return curr.second to curr.third
            }

            for ((state, cost) in curr.first.next) {
                if (visited.contains(state)) continue
                val log = curr.third.toMutableList()
                log.add(curr.first)
                val next = Triple(state, curr.second + cost, log)
                queue.add(next)
            }
        }

        return null
    }

    fun printState(s: State) {
        println("+-----------------------+")
        print("|")
        (0..10).forEach { print(" "); print(s.data[it]) }
        println(" |")
        print("+---")
        (0..3).forEach {
            print("|"); print(" "); print(s.data[it + 11]); print(" ")
        }
        println("|---+")
        for (i in 15 until s.data.size step 4) {
            print("   ")
            (0..3).forEach {
                print(" |"); print(" "); print(s.data[it + i])
            }
            println(" |")
        }
        println("    +---------------+")
    }

    fun parseInput(input: List<String>): State {
        var s = input[1].substring(1, 12)
        for (line in input.drop(2)) {
            if (line[3] == '#') continue
            s = s + line[3] + line[5] + line[7] + line[9]
        }
        return State(s.toList())
    }

    fun part1(input: List<String>): Int {
        rule = Rule(input.size - 3)
        val state = parseInput(input)
        return search(state)?.first ?: 0
    }

    fun part2(input: List<String>): Int {
        rule = Rule(input.size - 1)
        val input2 = input.toMutableList()
        input2.add(3, "  #D#C#B#A#")
        input2.add(4, "  #D#B#A#C#")
        val state = parseInput(input2)
        return search(state)?.first ?: 0
    }

    val day = 23
    val title = "Amphipod"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 12521)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 44169)
    print("Part Two: "); println(part2(input))
}
