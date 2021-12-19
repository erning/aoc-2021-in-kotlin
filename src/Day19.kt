fun main() {
    data class Point(val x: Int, val y: Int, val z: Int) {
        fun rotate(rotation: Int): Point {
            return when (rotation) {
                0 -> Point(x, y, z)
                1 -> Point(x, -z, y)
                2 -> Point(x, -y, -z)
                3 -> Point(x, z, -y)

                4 -> Point(-x, y, z)
                5 -> Point(-x, -z, y)
                6 -> Point(-x, -y, -z)
                7 -> Point(-x, z, -y)

                8 -> Point(y, x, z)
                9 -> Point(y, -z, x)
                10 -> Point(y, -x, -z)
                11 -> Point(y, z, -x)

                12 -> Point(-y, x, z)
                13 -> Point(-y, -z, x)
                14 -> Point(-y, -x, -z)
                15 -> Point(-y, z, -x)

                16 -> Point(z, y, x)
                17 -> Point(z, -x, y)
                18 -> Point(z, -y, -x)
                19 -> Point(z, x, -y)

                20 -> Point(-z, y, x)
                21 -> Point(-z, -x, y)
                22 -> Point(-z, -y, -x)
                23 -> Point(-z, x, -y)

                else -> Point(x, y, z)
            }
        }

        fun distanceSquare(other: Point): Int {
            val dx = other.x - x
            val dy = other.y - y
            val dz = other.z - z
            return dx * dx + dy * dy + dz * dz
        }
    }

    class Scanner(val beacons: List<Point>) {
        var _position: Point = Point(0, 0, 0)

        fun getBeacons(rotation: Int = 0): List<Point> {
            return beacons.map {
                it.rotate(rotation)
            }
        }
    }

    fun parsePoint(s: String): Point {
        return s.split(",")
            .map { it.toInt() }
            .let { Point(it[0], it[1], it[2]) }
    }

    fun parseInput(input: List<String>): List<Scanner> {
        val scanners: MutableList<Scanner> = mutableListOf()
        var i = 0

        fun parseBeacons(): List<Point> {
            val beacons: MutableList<Point> = mutableListOf()
            do {
                beacons.add(parsePoint(input[i++]))
            } while (i < input.size && input[i].isNotBlank())
            return beacons
        }

        do {
            val line = input[i++]
            if (line.startsWith("---")) {
                val beacons = parseBeacons()
                val scanner = Scanner(beacons)
                scanners.add(scanner)
            }
        } while (i < input.size)

        return scanners
    }

    fun part1(input: List<String>): Int {
        val scanners = parseInput(input)
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val day = 19
    val title = "Beacon Scanner"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 79)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 0)
    print("Part Two: "); println(part2(input))
}
