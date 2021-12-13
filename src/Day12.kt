fun main() {
    fun parseInput(input: List<String>): Map<String, List<String>> {
        val graph: MutableMap<String, MutableList<String>> = mutableMapOf()
        for (line in input) {
            val (a, b) = line.split("-")
            fun addCave(from: String, to: String) {
                val neighbors = graph[from] ?: mutableListOf()
                neighbors.add(to)
                graph[from] = neighbors
            }
            addCave(a, b)
            addCave(b, a)
        }
        return graph
    }

    fun part1(input: List<String>): Int {
        val graph = parseInput(input)
        val visited: MutableSet<String> = mutableSetOf()
        var pathCount = 0

        fun travel(cave: String) {
            if (cave == "end") {
                pathCount++
                return
            }
            if (cave != "start" && cave[0] in 'a'..'z') {
                if (visited.contains(cave)) {
                    return
                }
                visited.add(cave)
            }
            graph.getValue(cave).filter {
                it != "start"
            }.forEach {
                travel(it)
            }
            visited.remove(cave)
        }

        travel("start")
        return pathCount
    }

    fun part2(input: List<String>): Int {
        val graph = parseInput(input)
        val visited: MutableMap<String, Int> = mutableMapOf()
        var pathCount = 0

        fun travel(cave: String) {
            if (cave == "end") {
                pathCount++
                return
            }
            if (cave != "start" && cave[0] in 'a'..'z') {
                val times = visited[cave]
                if (times == null) {
                    visited[cave] = 1
                } else if (visited.values.any { it > 1 }) {
                    return
                } else {
                    visited[cave] = times + 1
                }
            }
            graph.getValue(cave).filter {
                it != "start"
            }.forEach {
                travel(it)
            }
            val times = visited.remove(cave) ?: return
            if (times > 1) {
                visited[cave] = times - 1
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
