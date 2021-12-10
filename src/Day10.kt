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
        for (chunks in input) {
            val stack: MutableList<Char> = mutableListOf()
            for (c in chunks.toCharArray()) {
                if (c in brackets.keys) {
                    stack.add(c)
                    continue
                }
                val oc = stack.removeLast()
                if (brackets[oc] == c) {
                    continue
                }
                score += pointsMap[c]!!
                break
            }
        }
        return score
    }

    fun part2(input: List<String>): ULong {
        val pointsMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
        val scores: MutableList<ULong> = mutableListOf()
        for (chunks in input) {
            val stack: MutableList<Char> = mutableListOf()
            for (c in chunks.toCharArray()) {
                if (c in brackets.keys) {
                    stack.add(c)
                    continue
                }
                val oc = stack.removeLast()
                if (brackets[oc] == c) {
                    continue
                }
                stack.clear()
                break
            }
            if (stack.isEmpty()) {
                continue
            }
            val score = stack.reversed().map {
                brackets[it]
            }.map {
                pointsMap[it]!!.toULong()
            }.reduce { acc, i ->
                acc * 5UL + i
            }
            scores.add(score)
        }
        scores.sort()
        return scores[scores.size / 2]
    }

    val day = 10

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 26397)
    check(part2(exampleInput) == 288957UL)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
