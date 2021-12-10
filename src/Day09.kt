fun main() {
    val neighbors = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

    fun findLowPoints(heightmap: List<List<Int>>): List<Pair<Int, Int>> {
        val rows = heightmap.size
        val cols = heightmap[0].size
        val lowPoints: MutableList<Pair<Int, Int>> = mutableListOf()
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                var ns = 0
                for ((dx, dy) in neighbors) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in 0 until cols && ny in 0 until rows && heightmap[ny][nx] <= heightmap[y][x]) {
                        break
                    }
                    ns++
                }
                if (ns >= 4) {
                    lowPoints.add(Pair(x, y))
                }
            }
        }
        return lowPoints
    }

    fun part1(input: List<String>): Int {
        val heightmap = input.map { s ->
            s.toCharArray().map { c ->
                c.digitToInt()
            }
        }
        val lowPoints = findLowPoints(heightmap)
        val riskLevel = lowPoints.sumOf { (x, y) ->
            heightmap[y][x] + 1
        }
        return riskLevel
    }

    fun part2(input: List<String>): Int {
        val heightmap = input.map { s ->
            s.toCharArray().map { c ->
                c.digitToInt()
            }.toMutableList()
        }.toMutableList()
        val rows = heightmap.size
        val cols = heightmap[0].size

        fun findBasinSize(lowPoint: Pair<Int, Int>): Int {
            val (x, y) = lowPoint
            heightmap[y][x] = 10
            var size = 1
            for ((dx, dy) in neighbors) {
                val nx = x + dx
                val ny = y + dy
                if (nx in 0 until cols && ny in 0 until rows && heightmap[ny][nx] < 9) {
                    size += findBasinSize(Pair(nx, ny))
                }
            }
            return size
        }

        val lowPoints = findLowPoints(heightmap)
        val basinSizes = lowPoints.map {
            findBasinSize(it)
        }
        return basinSizes
            .sorted()
            .takeLast(3)
            .reduce { acc, i -> acc * i }
    }

    val day = 9

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 15)
    check(part2(exampleInput) == 1134)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
