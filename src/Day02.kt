fun main() {
    fun part1(input: List<String>): Int {
        val commands = input.map {
            val v = it.split(" ")
            Pair(v[0], v[1].toInt())
        }
        var h = 0
        var d = 0
        for ((action, distance) in commands) {
            when (action) {
                "forward" -> h += distance
                "down" -> d += distance
                "up" -> d -= distance
            }
        }
        return h * d
    }

    fun part2(input: List<String>): Int {
        val commands = input.map {
            val v = it.split(" ")
            Pair(v[0], v[1].toInt())
        }
        var aim = 0
        var h = 0
        var d = 0
        for ((action, value) in commands) {
            when (action) {
                "forward" -> { h += value; d += aim * value }
                "down" -> aim += value
                "up" -> aim -= value
            }
        }
        return h * d
    }

    val day = 2

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 150)
    check(part2(exampleInput) == 900)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
