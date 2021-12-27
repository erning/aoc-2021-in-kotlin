import java.util.*

fun main() {
    fun parseInput(input: List<String>): Triple<Int, Int, Map<Pair<Int, Int>, Char>> {
        val height = input.size
        val width = input.first().length
        val sea: MutableMap<Pair<Int, Int>, Char> = mutableMapOf()
        for (y in input.indices) {
            for (x in input[y].indices) {
                when (input[y][x]) {
                    'v', '>' -> sea[x to y] = input[y][x]
                    '.'->{}
                    else -> error("???")
                }
            }
        }
        return Triple(width, height, sea)
    }

    fun part1(input: List<String>): Int {
        var (width, height, sea) = parseInput(input)
        var i = 0
        do {
//            println("*** $i ***")
//            for (y in 0 until height) {
//                for (x in 0 until width) {
//                    print(sea[x to y] ?: ".")
//                }
//                println()
//            }
//            println()

            i++
            var isMoved = false
            val removes: MutableList<Pair<Int, Int>> = mutableListOf()
            val newSea = sea.toMutableMap()
            sea.filter { it.value == '>' }.forEach {
                val (x, y) = it.key
                val nx = (x + 1) % width
                if (!sea.containsKey(nx to y)) {
                    newSea[nx to y] = it.value
                    removes.add(x to y)
                    isMoved = true
                }
            }

            removes.forEach { newSea.remove(it) }
            removes.clear()
            sea = newSea

            sea.filter { it.value == 'v' }.forEach {
                val (x, y) = it.key
                val ny = (y + 1) % height
                if (!sea.containsKey(x to ny)) {
                    newSea[x to ny] = it.value
                    removes.add(x to y)
                    isMoved = true
                }
            }
            removes.forEach { newSea.remove(it) }
            sea = newSea
        } while (isMoved)

        return i
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val day = 25
    val title = "Sea Cucumber"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 58)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == -1)
    print("Part Two: "); println(part2(input))
}
