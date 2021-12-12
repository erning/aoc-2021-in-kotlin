fun main() {
    fun parseGraph(input: List<String>): Map<String, List<String>> {
        val graph: MutableMap<String, MutableList<String>> = mutableMapOf()
        for (line in input) {
            val (a, b) = line.split("-")
            fun addCave(from: String, to: String) {
                if (from == "end") return
                if (to == "start") return
                if (!graph.containsKey(from)) {
                    graph[from] = mutableListOf()
                }
                graph.getValue(from).add(to)
            }
            addCave(a, b)
            addCave(b, a)
        }
        return graph
    }

    fun part1(input: List<String>): Int {
        val graph = parseGraph(input)
        val visited: MutableSet<String> = mutableSetOf()
        var pathCount = 0

        fun travel(cave: String) {
            if (cave == "end") {
                pathCount++
                return
            }
            if (cave[0] in 'a'..'z') {
                if (visited.contains(cave)) {
                    return
                }
                visited.add(cave)
            }
            for (next in graph.getValue(cave).filter { it != "start" }) {
                travel(next)
            }
            visited.remove(cave)
        }

        travel("start")
        return pathCount
    }

    fun part2(input: List<String>): Int {
        val graph = parseGraph(input)
        val visited: MutableMap<String, Int> = mutableMapOf()
        var pathCount = 0

        fun travel(cave: String) {
            if (cave == "end") {
                pathCount++
                return
            }
            if (cave[0] in 'a'..'z') {
                val visitedTimes = visited[cave]
                if (visitedTimes == null) {
                    visited[cave] = 1
                } else if (visited.values.any { it > 1 }) {
                    return
                } else {
                    visited[cave] = visitedTimes + 1
                }
            }
            for (next in graph.getValue(cave).filter { it != "start" }) {
                travel(next)
            }
            val visitedTimes = visited.remove(cave) ?: return
            if (visitedTimes > 1) {
                visited[cave] = visitedTimes - 1
            }
        }

        travel("start")
        return pathCount
    }

    val day = 12

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 10)
    check(part2(exampleInput) == 36)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
