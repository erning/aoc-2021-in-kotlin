fun main() {
    val brackets = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    fun part1(input: List<String>): Int {
        val pointsMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        var score = 0
        input.forEach {
            val stack: MutableList<Char> = mutableListOf()
            for (c in it) {
                if (c in brackets.keys) {
                    stack.add(c)
                    continue
                }
                if (c == brackets[stack.removeLastOrNull()]) {
                    continue
                }
                score += pointsMap[c]!!
                break
            }
        }
        return score
    }

    fun part2(input: List<String>): Long {
        val pointsMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
        val scores: MutableList<Long> = mutableListOf()
        input.forEach {
            val stack: MutableList<Char> = mutableListOf()
            for (c in it) {
                if (c in brackets.keys) {
                    stack.add(c)
                    continue
                }
                if (c == brackets[stack.removeLastOrNull()]) {
                    continue
                }
                stack.clear()
                break
            }
            if (stack.isNotEmpty()) {
                val score = stack.reversed().map { c ->
                    brackets[c]
                }.mapNotNull { c ->
                    pointsMap[c]
                }.map { i ->
                    i.toLong()
                }.reduce { acc, i ->
                    acc * 5L + i
                }
                scores.add(score)
            }
        }
        scores.sort()
        return scores[scores.size / 2]
    }

    val day = 10

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 26397)
    check(part2(exampleInput) == 288957L)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
