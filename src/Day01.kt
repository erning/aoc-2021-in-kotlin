fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { it.toInt() }
        return numbers.zip(numbers.drop(1)).count {
            it.first < it.second
        }
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { it.toInt() }
        return part1(
            numbers.dropLast(2).indices.map {
                numbers[it] + numbers[it + 1] + numbers[it + 2]
            }.map {
                it.toString()
            }
        )
    }

    val day = 1

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 7)
    check(part2(exampleInput) == 5)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
