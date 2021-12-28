fun main() {
    fun search(program: List<String>, digits: List<Int>): String? {
        val visited: MutableSet<Pair<Int, Map<Char, Long>>> = mutableSetOf()
        var final: String? = null

        fun exec(pc: Int, regs: Map<Char, Long>, ds: List<Int>): Boolean {
            if (visited.contains(pc to regs)) return false
            visited.add(pc to regs)
            if ((regs['z'] ?: 0) > 1000000L) return false
            
            if (pc >= program.size) {
                if (regs['z'] == 0L) {
                    final = ds.joinToString("") { it.toString() }
                    return true
                }
                return false
            }

            val rv = regs.toMutableMap()
            val inst = program[pc]
            val op = inst.substring(0, 3)
            val a = inst.substring(4, 5)
            if (op == "inp") {
                for (d in digits) {
                    rv[a[0]] = d.toLong()
                    if (exec(pc + 1, rv, ds + d)) return true
                }
                return false
            }
            val b = inst.substring(6)
            val av = regs[a[0]] ?: 0
            val bv: Long = b.toLongOrNull() ?: (regs[b[0]] ?: 0)
            rv[a[0]] = when (op) {
                "add" -> av + bv
                "mul" -> av * bv
                "div" -> av / bv
                "mod" -> av % bv
                "eql" -> if (av == bv) 1 else 0
                else -> error("unknown operator")
            }
            return exec(pc + 1, rv, ds)
        }
        exec(0, mapOf(), listOf())
        return final
    }

    fun part1(input: List<String>): String? {
        return search(input, (1..9).reversed().toList())
    }

    fun part2(input: List<String>): String? {
        return search(input, (1..9).toList())
    }

    val day = 24
    val title = "Arithmetic Logic Unit"

    val input = readInput(day)

    println("--- Day $day: $title ---")

    print("Part One: "); println(part1(input))
    print("Part Two: "); println(part2(input))
}
