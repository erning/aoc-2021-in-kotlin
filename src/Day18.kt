fun main() {
    class Node(
        private var _value: Int? = null,
        private var _lhs: Node? = null,
        private var _rhs: Node? = null,
    ) {
        constructor(value: Int) : this(value, null, null)
        constructor(lhs: Node, rhs: Node) : this(null, lhs, rhs)

        val isNumber: Boolean get() = _value != null
        val isPair: Boolean get() = !isNumber && lhs.isNumber && rhs.isNumber

        var value: Int
            get() = _value!!
            set(nv) {
                _value = nv; _lhs = null; _rhs = null
            }

        var lhs: Node
            get() = _lhs!!
            set(nv) {
                _value = null; _lhs = nv
            }

        var rhs: Node
            get() = _rhs!!
            set(nv) {
                _value = null; _rhs = nv
            }

        override fun toString(): String {
            return if (isNumber) "$value" else "[$lhs,$rhs]"
        }

        fun _explode(): Boolean {
            val found = fun(): Triple<Node?, Node?, Node?> {
                var en: Node? = null
                var ln: Node? = null
                var rn: Node? = null
                fun find(node: Node, depth: Int) {
                    if (node.isNumber) {
                        if (en == null) {
                            ln = node
                        } else if (rn == null) {
                            rn = node
                        }
                    } else if (en == null && depth >= 4) {
                        en = node
                    } else if (en == null || rn == null) {
                        find(node.lhs, depth + 1)
                        find(node.rhs, depth + 1)
                    }
                }
                find(this, 0)
                return Triple(en, ln, rn)
            }()

            val en = found.first ?: return false
            val ln = found.second
            if (ln != null) {
                ln.value += en.lhs.value
            }
            val rn = found.third
            if (rn != null) {
                rn.value += en.rhs.value
            }
            en.value = 0
            return true
        }

        fun _split(): Boolean {
            fun find(node: Node): Node? {
                return if (node.isNumber) {
                    if (node.value >= 10) node else null
                } else {
                    find(node.lhs) ?: find(node.rhs)
                }
            }

            val node = find(this) ?: return false
            val v1 = node.value / 2
            val v2 = node.value % 2 + v1
            node.lhs = Node(v1)
            node.rhs = Node(v2)
            return true
        }

        fun _reduce(): Boolean {
            var count = 0
            while (_explode() || _split()) count++
            return count > 0
        }

        fun _add(other: Node): Node {
            return Node(this, other)
        }

        fun magnitude(node: Node = this): Int {
            return if (node.isNumber)
                node.value
            else
                magnitude(node.lhs) * 3 + magnitude(node.rhs) * 2
        }
    }

    fun parseNode(data: String): Node {
        val stack: MutableList<Node> = mutableListOf()
        var i = 0
        fun buildNode(): Node {
            while (i < data.length) {
                val c = data[i++]
                if (c.isDigit()) {
                    var n = c.digitToInt()
                    var j = i
                    while (j < data.length && data[j].isDigit()) {
                        n = n * 10 + data[j].digitToInt()
                        j++
                    }
                    i = j
                    return Node(n)
                }
                when (c) {
                    '[' -> stack.add(buildNode())
                    ',' -> stack.add(buildNode())
                    ']' -> {
                        val rhs = stack.removeLast()
                        val lhs = stack.removeLast()
                        return Node(lhs, rhs)
                    }
                }
            }
            error("invalid")
        }
        return buildNode()
    }

    fun Node.copy(): Node {
        return parseNode(this.toString())
    }

    fun Node.reduce(): Node {
        val node = this.copy()
        node._reduce()
        return node
    }

    fun Node.add(other: Node): Node {
        val node = this.copy()
        return node._add(other)
    }

    fun part1(input: List<String>): Int {
        return input
            .map { parseNode(it) }
            .reduce { acc, it -> acc.add(it).reduce() }
            .magnitude()
    }

    fun part2(input: List<String>): Int {
        var max = Int.MIN_VALUE
        for (i in input.indices) {
            for (j in input.indices) {
                if (i == j) continue
                val n1 = parseNode(input[i])
                val n2 = parseNode(input[j])
                val m = n1.add(n2).reduce().magnitude()
                if (m > max) max = m
            }
        }
        return max
    }

    //

    fun testExplode() {
        val cases: List<Pair<String, String>> = listOf(
            Pair("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]"),
            Pair("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]"),
            Pair("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]"),
            Pair("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
            Pair("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"),
        )
        for (case in cases) {
            val node = parseNode(case.first)
            node._explode()
            if (node.toString() != case.second) {
                error("expect: ${case.second}, but: $node")
            }
        }
    }

    fun testSplit() {
        val cases: List<Pair<String, String>> = listOf(
            Pair("[[[[0,7],4],[15,[0,13]]],[1,1]]", "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"),
            Pair("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"),
        )
        for (case in cases) {
            val node = parseNode(case.first)
            node._split()
            if (node.toString() != case.second) {
                error("expect: ${case.second}, but: $node")
            }
        }
    }

    fun testReduce() {
        val cases: List<Pair<String, String>> = listOf(
            Pair("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"),
        )
        for (case in cases) {
            val node = parseNode(case.first)
            node._reduce()
            if (node.toString() != case.second) {
                error("expect: ${case.second}, but: $node")
            }
        }
    }

    testExplode()
    testSplit()
    testReduce()

    //

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
