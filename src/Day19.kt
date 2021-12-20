import kotlin.math.abs

fun main() {
    data class Point(val x: Int, val y: Int, val z: Int) {
        operator fun plus(p: Point): Point {
            return Point(x + p.x, y + p.y, z + p.z)
        }

        operator fun minus(p: Point): Point {
            return Point(x - p.x, y - p.y, z - p.z)
        }

        operator fun unaryMinus(): Point {
            return Point(-x, -y, -z)
        }

        fun rotate(orientation: Int): Point {
            return when (orientation) {
                0 -> Point(x, y, z)
                1 -> Point(x, -z, y)
                2 -> Point(x, -y, -z)
                3 -> Point(x, z, -y)

                4 -> Point(-x, y, -z)
                5 -> Point(-x, -z, -y)
                6 -> Point(-x, -y, z)
                7 -> Point(-x, z, y)

                8 -> Point(y, x, -z)
                9 -> Point(y, -z, -x)
                10 -> Point(y, -x, z)
                11 -> Point(y, z, x)

                12 -> Point(-y, x, z)
                13 -> Point(-y, -z, x)
                14 -> Point(-y, -x, -z)
                15 -> Point(-y, z, -x)

                16 -> Point(z, y, -x)
                17 -> Point(z, x, y)
                18 -> Point(z, -y, x)
                19 -> Point(z, -x, -y)

                20 -> Point(-z, y, x)
                21 -> Point(-z, -x, y)
                22 -> Point(-z, -y, -x)
                23 -> Point(-z, x, -y)

                else -> error("invalid")
            }
        }

        fun manhattanDistance(other: Point): Int {
            val p = other - this
            return abs(p.x) + abs(p.y) + abs(p.z)
        }
    }

    data class Scanner(val position: Point, val beacons: Set<Point>) {
        constructor(beacons: Set<Point>) : this(Point(0, 0, 0), beacons)
        constructor(beacons: List<Point>) : this(Point(0, 0, 0), beacons.toSet())
        constructor(position: Point, beacons: List<Point>) : this(position, beacons.toSet())

        fun move(newPosition: Point): Scanner {
            val delta = newPosition - position
            return Scanner(newPosition, beacons.map { it + delta })
        }

        fun rotate(orientation: Int): Scanner {
            return Scanner(beacons.map { it.rotate(orientation) })
        }

        fun intersect(other: Scanner): Int {
            return beacons.intersect(other.beacons).size
        }

        fun match(other: Scanner): Scanner? {
            for (beacon in beacons) {
                for (orientation in 0..23) {
                    val rotated = other.rotate(orientation)
                    for (otherBeacon in rotated.beacons) {
                        val p = position + beacon - otherBeacon
                        val scanner = rotated.move(p)
                        if (intersect(scanner) >= 12) {
                            return scanner
                        }
                    }
                }
            }
            return null
        }
    }

    fun parseInput(input: List<String>): List<Scanner> {
        val scanners: MutableList<Scanner> = mutableListOf()
        var i = 0

        fun parsePoint(s: String): Point {
            return s.split(",")
                .map { it.toInt() }
                .let { Point(it[0], it[1], it[2]) }
        }

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
        val remaining: MutableList<Int> = scanners.indices.drop(1).toMutableList()
        var base = scanners.first()

        while (remaining.isNotEmpty()) {
            val other = remaining.removeFirst()
            val scanner = base.match(scanners[other])
            if (scanner != null) {
                base = Scanner(base.beacons.union(scanner.beacons))
            } else {
                remaining.add(other)
            }
        }
        return base.beacons.size
    }

    fun part2(input: List<String>): Int {
        val scanners = parseInput(input)
        val remaining: MutableList<Int> = scanners.indices.drop(1).toMutableList()
        var base = scanners.first()
        val positions: MutableList<Point> = mutableListOf(base.position)

        while (remaining.isNotEmpty()) {
            val other = remaining.removeFirst()
            val scanner = base.match(scanners[other])
            if (scanner != null) {
                base = Scanner(base.beacons.union(scanner.beacons))
                positions.add(scanner.position)
            } else {
                remaining.add(other)
            }
        }

        return positions.maxOf { a ->
            positions.maxOf { b ->
                a.manhattanDistance(b)
            }
        }
    }

    val day = 19
    val title = "Beacon Scanner"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 79)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 3621)
    print("Part Two: "); println(part2(input))
}
