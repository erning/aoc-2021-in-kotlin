fun main() {
    fun parseInput(input: List<String>): Pair<String, Map<String, Char>> {
        val template = input.first()
        val rules = mutableMapOf<String, Char>()
        (2 until input.size).map { i ->
            input[i].split(" -> ")
        }.forEach { (a, b) ->
            rules[a] = b[0]
        }
        return Pair(template, rules)
    }

    fun merge(a: Map<Char, Long>, b: Map<Char, Long>): Map<Char, Long> {
        val c = a.toMutableMap()
        for ((k, v) in b) {
            val x = c[k] ?: 0
            c[k] = x + v
        }
        return c
    }

    fun search(input: List<String>, maxDepth: Int): Long {
        val (template, rules) = parseInput(input)
        val quantity: MutableMap<Char, Long> = mutableMapOf()
        val cache: MutableMap<Pair<String, Int>, Map<Char, Long>> = mutableMapOf()

        fun dfs(node: String, depth: Int): Map<Char, Long> {
            if (depth <= 0) {
                return mapOf()
            }
            val ch = rules[node] ?: return mapOf()

            val cacheKey = Pair(node, depth)
            val cached = cache[cacheKey]
            if (cached != null) return cached

            val q1 = dfs("" + node[0] + ch, depth - 1)
            val q2 = dfs("" + ch + node[1], depth - 1)
            val q3 = merge(q1, q2).toMutableMap()
            val q = q3[ch] ?: 0
            q3[ch] = q + 1

            cache[cacheKey] = q3
            return q3
        }

        template.forEach {
            val q = quantity[it] ?: 0
            quantity[it] = q + 1
        }
        for (i in 1 until template.length) {
            val node = "" + template[i - 1] + template[i]
            val q = dfs(node, maxDepth)
            for ((k, v) in q) {
                val x = quantity[k] ?: 0
                quantity[k] = x + v
            }
        }

        val maxValue = quantity.maxOf { it.value }
        val minValue = quantity.minOf { it.value }

        return maxValue - minValue
    }

    fun part1(input: List<String>): Long {
        return search(input, 10)
    }

    fun part2(input: List<String>): Long {
        return search(input, 40)
    }

    val day = 14

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 1588L)
    check(part2(exampleInput) == 2188189693529L)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
