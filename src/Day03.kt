fun main() {
    fun part1(input: List<String>): Int {
        val cols = input[0].length
        val grid = (0 until cols).map { i ->
            input.map {
                it[i].digitToInt()
            }
        }
        val gamma = grid.map {
            it.sumOf { c ->
                c * 2 - 1
            }
        }.map {
            if (it >= 0) 1 else 0
        }
        val epsilon = gamma.map {
            if (it == 0) 1 else 0
        }
        val gammaRate = gamma.reduce { acc, v ->
            acc.shl(1).or(v)
        }
        val epsilonRate = epsilon.reduce { acc, v ->
            acc.shl(1).or(v)
        }

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): Int {
        val cols = input[0].length
        fun rating(a: Int, b: Int): Int {
            var grid = input.toMutableList()
            for (i in 0 until cols){
                val v = grid.map {
                    it[i].digitToInt()
                }.sumOf { c ->
                    c * 2 - 1
                }
                val bit = when {
                    v > 0 -> a
                    v < 0 -> b
                    else -> a
                }
                grid = grid.filter {
                    it[i].digitToInt() == bit
                }.toMutableList()
                if (grid.size <= 1) {
                    break
                }
            }
            return grid.first().map {
                it.digitToInt()
            }.reduce { acc, v ->
                acc.shl(1).or(v)
            }
        }

        val oxygenRating = rating(1, 0)
        val co2Rating = rating(0, 1)
        return oxygenRating * co2Rating
    }

    val day = 3

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 198)
    check(part2(exampleInput) == 230)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
