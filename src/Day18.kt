fun main() {
    class Node(var value: Int? = null, var first: Node? = null, var second: Node? = null) {
        constructor(value: Int) : this(value, null, null)
        constructor(first: Node, second: Node) : this(null, first, second)

        val isNumber: Boolean
            get() {
                return value != null
            }

        val isPair: Boolean
            get() {
                if (isNumber) return false
                return (first!!.isNumber && second!!.isNumber)
            }

        override fun toString(): String {
            if (isNumber) return "$value"
//            if (isPair)  return "<$first, $second>"
            return "[$first, $second]"
        }

        fun explode(): Boolean {
            var toExplode: Node? = null
            var left: Node? = null
            var right: Node? = null

            fun find(node: Node, depth: Int = 0) {
                if (node.isNumber) {
                    if (toExplode == null) {
                        left = node
                    } else {
                        if (right == null) {
                            if (node != toExplode!!.first && node != toExplode!!.second) {
                                right = node
                            }
                        }
                    }
                    return
                }

                if (node.isPair) {
                    if (depth >= 4 && toExplode == null) {
                        toExplode = node
                    }
                }
                find(node.first!!, depth + 1)
                find(node.second!!, depth + 1)
            }
            find(this)

            if (toExplode == null) {
                return false
            }

            if (left != null) {
                left!!.value = toExplode!!.first!!.value!! + left!!.value!!
            }
            if (right != null) {
                right!!.value = toExplode!!.second!!.value!! + right!!.value!!
            }
            toExplode!!.value = 0
            toExplode!!.first = null
            toExplode!!.second = null

            return true
        }

        fun split(): Boolean {
            var found = false
            fun find(node: Node) {
                if (found) return
                if (node.isNumber) {
                    val v = node.value ?: return
                    if (v >= 10) {
                        node.value = null
                        node.first = Node(v / 2)
                        node.second = Node(v / 2 + v % 2)
                        found = true
                    }
                    return
                }
                find(node.first!!)
                find(node.second!!)
            }
            find(this)
            return found
        }

        fun reduce(): Boolean {
            var count = 0
            while (explode() || split()) {
                count++
            }
            return count > 0
        }

        fun add(other: Node): Node {
            return Node(this, other)
        }

        fun magnitude(): Int {
            fun calc(node: Node): Int {
                if (node.isNumber) return node.value!!
                return calc(node.first!!) * 3 + calc(node.second!!) * 2
            }
            return calc(this)
        }
    }

    fun parseNode(data: String): Node {
        val stack: MutableList<Node> = mutableListOf()
        var i = 0
        fun buildNode(): Node {
            while (i < data.length) {
                val c = data[i++]
                if (c.isDigit()) {
                    return Node(c.digitToInt())
                }
                when (c) {
                    '[' -> stack.add(buildNode())
                    ',' -> stack.add(buildNode())
                    ']' -> {
                        val second = stack.removeLast()
                        val first = stack.removeLast()
                        return Node(first, second)
                    }
                    else -> error("?")
                }
            }
            error("?")
        }
        return buildNode()
    }

    fun parseInput(input: List<String>): List<Node> {
        return input.map { parseNode(it) }
    }

    fun part1(input: List<String>): Int {
        val nodes = parseInput(input)
        var node = nodes.first()
        for (next in nodes.drop(1)) {
            node = node.add(next)
            node.reduce()
        }
        return node.magnitude()
    }

    fun part2(input: List<String>): Int {
        var max = Int.MIN_VALUE
        for (i in input.indices) {
            for (j in input.indices) {
                if (i == j) continue
                val n1 = parseNode(input[i])
                val n2 = parseNode(input[j])
                var node = n1.add(n2)
                node.reduce()
                val m = node.magnitude()
                if (m > max) max = m
            }
        }
        return max
    }

    val day = 18
    val title = "Snailfish"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 4140)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 3993)
    print("Part Two: "); println(part2(input))
}
