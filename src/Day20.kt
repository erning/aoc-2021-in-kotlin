fun main() {
    class Image(val data: List<String>) {
        val width = data.first().length
        val height = data.size

        override fun toString(): String {
            return data.joinToString(separator = "\n")
        }

        val lightPixels: Int get() = data.sumOf { it.count { c -> c == '#' } }

        fun getPixel(x: Int, y: Int, default: Char = '.'): Char {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return default
            }
            return data[y][x]
        }

        fun enhance(enhancement: String, times: Int): Image {
            var image = this
            var default = '.'
            repeat(times) {
                val output: MutableList<String> = mutableListOf()
                for (y in -1 until image.height + 1) {
                    var row = ""
                    for (x in -1 until image.width + 1) {
                        val idx = (0..8).map {
                            image.getPixel(it % 3 - 1 + x, it / 3 - 1 + y, default)
                        }.map {
                            if (it == '#') 1 else 0
                        }.reduce { acc, v ->
                            acc.shl(1).or(v)
                        }
                        row += enhancement[idx]
                    }
                    output.add(row)
                }
                image = Image(output)
                if (enhancement[0] == '#') {
                    default = if (default == '#') '.' else '#'
                }
            }
            return image
        }
    }

    fun parseInput(input: List<String>): Pair<String, Image> {
        return Pair(
            input.first(),
            Image(input.drop(2))
        )
    }

    fun part1(input: List<String>): Int {
        val (enhancement, image) = parseInput(input)
        return image.enhance(enhancement, 2).lightPixels
    }

    fun part2(input: List<String>): Int {
        val (enhancement, image) = parseInput(input)
        return image.enhance(enhancement, 50).lightPixels
    }

    val day = 20
    val title = "Trench Map"

    val exampleInput = readExample(day)
    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(exampleInput) == 35)
    print("Part One: "); println(part1(input))

    check(part2(exampleInput) == 3351)
    print("Part Two: "); println(part2(input))
}

/*
# Day 20: Trench Map

For day20, this puzzle was simple as compared to previous days puzzles.

The only tricky part was : **Infinite image**

So, if the image is infinite, the pixels outside the image boundry will all be the same.

For e.g. in the initial (original) image, if you take a section of 9x9 pixels far far away from the given part, it will be :
```
. . .
. . .
. . .
```
The decimal equivalent of this will always be 0 and at 0 index of image enhancement algorithm, we have `#`

So, after 1st iteration, all infinite pixels outside the image boundary will become `#` ..

And in the next step, the 9x9 pixels will be :
```
# # #
# # #
# # #
```
The decimal equivalent of this will always be 511 and at that index of image enhancement algorithm, we have `.`

So after every even steps, all the `#` pixels will become `.`.

Once this is clear, then its simple loop / replace logic.
 */
