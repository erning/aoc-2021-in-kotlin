fun main() {
    fun part1(input: List<String>): Int {
        val lanternfish = input[0].split(",").map {
            it.toInt()
        }.toMutableList()

        repeat(80) {
            val length = lanternfish.size
            for (i in 0 until length) {
                if (lanternfish[i] == 0) {
                    lanternfish[i] = 6
                    lanternfish.add(8)
                } else {
                    lanternfish[i] -= 1
                }
            }
        }
        return lanternfish.size
    }

    fun part2(input: List<String>): Long {
        val lanternfish = input[0].split(",").map {
            it.toInt()
        }
        val timers = LongArray(9)
        for (timer in lanternfish) {
            timers[timer] += 1L
        }

        repeat(256) {
            val new = timers[0]
            for (i in 1 until timers.size) {
                timers[i-1] = timers[i]
            }
            timers[8] = new
            if (new > 0L) {
                timers[6] += new
            }
        }

        return timers.sum()
    }

    val day = 6

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 5934)
    check(part2(exampleInput) == 26984457539)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
