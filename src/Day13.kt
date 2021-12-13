fun main() {
    fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<Pair<Char, Int>>> {
        val dots = input.filter {
            it.matches(Regex("\\d+,\\d+"))
        }.map {
            it.split(",")
        }.map { (a, b) ->
            Pair(a.toInt(), b.toInt())
        }

        val folds = input.filter {
            it.startsWith("fold")
        }.map {
            it.substring(startIndex = "fold along ".length)
        }.map {
            it.split("=")
        }.map { (a, b) ->
            Pair(a[0], b.toInt())
        }
        return Pair(dots, folds)
    }

    fun foldUp(dots: List<Pair<Int, Int>>, foldY: Int): List<Pair<Int, Int>> {
        return dots.filter {
            it.second > foldY
        }.map { (x, y) ->
            Pair(x, foldY * 2 - y)
        }.union(dots.filter { it.second < foldY} ).distinct()
    }

    fun foldLeft(dots: List<Pair<Int, Int>>, foldX: Int): List<Pair<Int, Int>> {
        return dots.filter {
            it.first > foldX
        }.map { (x, y) ->
            Pair(foldX * 2 - x, y)
        }.union(dots.filter { it.first < foldX} ).distinct()
    }

    fun fold(dots: List<Pair<Int, Int>>, fold: Pair<Char, Int>): List<Pair<Int, Int>> {
        return if (fold.first == 'y') foldUp(dots, fold.second) else foldLeft(dots, fold.second)
    }

    fun dotsToString(dots: List<Pair<Int, Int>>): String {
        val w = dots.maxOf { it.first }
        val h = dots.maxOf { it.second}
        var s = "\n"
        for (y in 0..h) {
            for (x in 0..w) {
                s += if (Pair(x, y) in dots) " #" else "  "
            }
            s += "\n"
        }
        return s
    }

    fun part1(input: List<String>): Int {
        var (dots, folds) = parseInput(input)
        dots = fold(dots, folds.first())
        return dots.size
    }


    fun part2(input: List<String>): String {
        var (dots, folds) = parseInput(input)
        folds.forEach {
            dots = fold(dots, it)
        }
        return dotsToString(dots)
    }

    val day = 13
    val title = "Transparent Origami"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 17)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput).isNotEmpty())
    print("Part Two: "); println(part2(input))
}
