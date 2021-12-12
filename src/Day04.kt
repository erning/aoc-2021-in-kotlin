fun main() {
    fun parseNumbers(input: List<String>): List<Int> {
        return input[0].split(",").map { it.toInt() }
    }

    fun parseBoards(input: List<String>): MutableList<MutableList<Int>> {
        val boards: MutableList<MutableList<Int>> = mutableListOf()
        for (i in 1 until input.size step 6) {
            val board = mutableListOf<Int>()
            for (j in 1..5) {
                board.addAll(
                    input[i + j].trimStart().split(Regex("\\s+")).map {
                        it.toInt()
                    }
                )
            }
            boards.add(board)
        }
        return boards
    }

    fun findPosition(number: Int, board: List<Int>): Int {
        for ((i, n) in board.withIndex()) {
            if (n == number) {
                return i
            }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        val numbers = parseNumbers(input)
        val boards = parseBoards(input)

        fun findWinner(): Pair<Int, Int> {
            val boardXY = Array(boards.size) { IntArray(10) }
            for (number in numbers) {
                for ((i, board) in boards.withIndex()) {
                    val pos = findPosition(number, board)
                    if (pos < 0) {
                        continue
                    }
                    val x = pos % 5
                    val y = pos / 5
                    boardXY[i][x] += 1
                    boardXY[i][5 + y] += 1
                    board[pos] = -1

                    if (boardXY[i][x] >= 5 || boardXY[i][5 + y] >= 5) {
                        return Pair(number, i)
                    }
                }
            }
            return Pair(0, 0)
        }

        val (number, i) = findWinner()
        val s = boards[i].filter { it > 0 }.sum()
        return s * number
    }

    fun part2(input: List<String>): Int {
        val numbers = parseNumbers(input)
        val boards = parseBoards(input)

        fun findLastWinner(): Pair<Int, Int> {
            val boardXY = Array(boards.size) { IntArray(10) }
            for (number in numbers) {
                for ((i, board) in boards.withIndex()) {
                    val pos = findPosition(number, board)
                    if (pos < 0) {
                        continue
                    }
                    val x = pos % 5
                    val y = pos / 5
                    boardXY[i][x] += 1
                    boardXY[i][5 + y] += 1
                    board[pos] = -1
                    if (boardXY[i][x] < 5 && boardXY[i][5 + y] < 5) {
                        continue
                    }
                    if (boards.count { it.size > 0 } == 1) {
                        return Pair(number, i)
                    }
                    board.clear()
                }
            }
            return Pair(0, 0)
        }

        val (number, i) = findLastWinner()
        val s = boards[i].filter { it > 0 }.sum()
        return s * number
    }

    val day = 4

    val exampleInput = readExample(day)
    check(part1(exampleInput) == 4512)
    check(part2(exampleInput) == 1924)

    val input = readInput(day)
    println(part1(input))
    println(part2(input))
}
