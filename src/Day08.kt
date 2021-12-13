fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            it.split(Regex("\\s*\\|\\s*"))
        }.map {
            it[1].split(" ")
        }.sumOf {
            it.count { s ->
                when (s.length) {
                    2, 3, 4, 7 -> true
                    else -> false
                }
            }
        }
    }

    fun buildCodeMap(input: List<String>): Map<String, Int> {
        val codeToNumber: MutableMap<String, Int> = mutableMapOf()
        val numberToCode = Array(10) { "" }

        fun setCode(code: String, number: Int) {
            codeToNumber[code] = number
            numberToCode[number] = code
        }

        for (code in input) {
            when (code.length) {
                2 -> setCode(code, 1)
                3 -> setCode(code, 7)
                4 -> setCode(code, 4)
                7 -> setCode(code, 8)
            }
        }
        for (code in input.filter { it.length == 6 }) {
            if (numberToCode[4].length == numberToCode[4].count { it in code }) {
                setCode(code, 9)
            } else if (numberToCode[7].length == numberToCode[7].count { it in code }) {
                setCode(code, 0)
            } else {
                setCode(code, 6)
            }
        }
        for (code in input.filter { it.length == 5 }) {
            if (numberToCode[7].length == numberToCode[7].count { it in code }) {
                setCode(code, 3)
            } else if (4 == numberToCode[6].count { it in code }) {
                setCode(code, 2)
            } else {
                setCode(code, 5)
            }
        }
        return codeToNumber
    }

    fun part2(input: List<String>): Int {
        val parsedInput = input.map {
            it.split(Regex("\\s*\\|\\s*"))
        }.map { (a, b) ->
            Pair(
                a.split(" ").map {
                    it.toCharArray().sorted().joinToString("")
                },
                b.split(" ").map {
                    it.toCharArray().sorted().joinToString("")
                }
            )
        }

        return parsedInput.sumOf {
            val codeToNumber = buildCodeMap(it.first)
            it.second.mapNotNull { code ->
                codeToNumber[code]
            }.reduce { acc, i ->
                acc * 10 + i
            }
        }
    }

    val day = 8

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 26)
    check(part2(exampleInput) == 61229)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
