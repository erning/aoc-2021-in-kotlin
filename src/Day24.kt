import java.io.Reader
import java.io.StringReader
import java.nio.CharBuffer

fun main() {
    val registers = setOf("w", "x", "y", "z")

    class ALU {
        lateinit var input: Reader
        val variables: MutableMap<String, Long> = mutableMapOf()

        override fun toString(): String {
            return "ALU: w=${variables["w"] ?: 0} x=${variables["x"] ?: 0} y=${variables["y"] ?: 0} z=${variables["z"] ?: 0}"
        }

        operator fun set(a: String, b: String) {
            set(a, if (b in registers) get(b) else b.toLong())
        }

        operator fun set(a: String, b: Long) {
            assert(a.length == 1)
            assert(a in registers)
            variables[a] = b
        }

        operator fun get(a: String): Long {
            return a.toLongOrNull() ?: variables[a] ?: 0L
        }

        val instructions: Map<String, (String, String) -> Unit> = mutableMapOf(
            "inp" to { a, _ -> set(a, input.read().toLong()) },
            "add" to { a, b -> set(a, get(a) + get(b)) },
            "mul" to { a, b -> set(a, get(a) * get(b)) },
            "div" to { a, b -> set(a, get(a) / get(b)) },
            "mod" to { a, b -> set(a, get(a) % get(b)) },
            "eql" to { a, b -> set(a, if (get(a) == get(b)) 1 else 0) }
        )

        fun execute(instruction: String) {
            val args = instruction.split(" ")
            val op = args[0]
            val a = args[1]
            val b = if (args.size >= 3) args[2] else ""
            val f = instructions[op] ?: return
            f(a, b)
        }

        fun reset() {
            variables.clear()
        }
    }

    class DigitReader(s: String) : StringReader(s) {
        override fun read(): Int {
            return super.read() - 48
        }
    }


    fun runProgram(input: String, instructions: List<String>): ALU {
        val alu = ALU()
        alu.input = DigitReader(input)
        instructions.forEach { alu.execute(it) }
        return alu
    }

    fun testALU1() {
        val program = """
            inp x
            mul x -1
        """.trimIndent().split("\n")
        val alu = runProgram("1", program)
        println(alu)
    }

    fun testALU2() {
        val program = """
            inp z
            inp x
            mul z 3
            eql z x
        """.trimIndent().split("\n")
        val alu = runProgram("13", program)
        println(alu)
    }

    fun testALU3() {
        val program = """
            inp w
            add z w
            mod z 2
            div w 2
            add y w
            mod y 2
            div w 2
            add x w
            mod x 2
            div w 2
            mod w 2
        """.trimIndent().split("\n")
        val alu = runProgram("7", program)
        println(alu)
    }

    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val day = 24
    val title = "Arithmetic Logic Unit"

    testALU1()
    testALU2()
    testALU3()

    val input = readInput(day)

    println("--- Day $day: $title ---")

    print("Part One: "); println(part1(input))
    print("Part Two: "); println(part2(input))
}
