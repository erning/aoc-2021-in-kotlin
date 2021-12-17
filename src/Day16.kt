fun main() {
    fun parseInput(input: List<String>): String {
        return input.first().map {
            it.digitToInt(16).toString(2).padStart(4, '0')
        }.joinToString(separator = "")
    }

    class BitReader(val binary: String) {
        var p: Int = 0

        fun read(): Int {
            return binary[p++].digitToInt()
        }

        fun read(count: Int): String {
            val bits = binary.substring(p, p + count)
            p += count
            return bits
        }

        fun readInt(count: Int): Int {
            return read(count).toInt(2)
        }
    }

    data class Packet(
        val version: Int, val typeID: Int, val value: Long,
        val children: List<Packet>? = null
    )

    class PacketDecoder(val br: BitReader) {
        fun decode(): Packet {
            val version = br.readInt(3)
            val typeID = br.readInt(3)
            if (typeID == 4) {
                return Packet(version, typeID, decodeLiteralValue())
            }
            val children = decodeSubPackets()
            val subvalues = children.map { it.value }
            val value: Long = when (typeID) {
                0 -> subvalues.sum()
                1 -> subvalues.reduce { acc, l -> acc * l }
                2 -> subvalues.minOrNull() ?: 0
                3 -> subvalues.maxOrNull() ?: 0
                5 -> subvalues.let { (a, b) -> if (a > b) 1 else 0 }
                6 -> subvalues.let { (a, b) -> if (a < b) 1 else 0 }
                7 -> subvalues.let { (a, b) -> if (a == b) 1 else 0 }
                else -> 0
            }
            return Packet(version, typeID, value, children)
        }

        fun decodeLiteralValue(): Long {
            var value = 0L
            do {
                val v = br.readInt(5).toLong()
                value = value.shl(4).or(v.and(0x0F))
            } while (v.and(0x10) != 0L)
            return value
        }

        fun decodeSubPackets(): List<Packet> {
            val children: MutableList<Packet> = mutableListOf()
            if (br.read() == 0) {
                val end = br.readInt(15) + br.p
                while (br.p < end) {
                    children.add(decode())
                }
            } else {
                val count = br.readInt(11)
                repeat(count) { children.add(decode()) }
            }
            return children
        }
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)
        val reader = BitReader(parsedInput)
        val decoder = PacketDecoder(reader)
        val packet = decoder.decode()

        fun sumOfVersion(packet: Packet): Int {
            var sum = packet.version
            packet.children?.forEach {
                sum += sumOfVersion(it)
            }
            return sum
        }
        return sumOfVersion(packet)
    }

    fun part2(input: List<String>): Long {
        val parsedInput = parseInput(input)
        val reader = BitReader(parsedInput)
        val decoder = PacketDecoder(reader)
        val packet = decoder.decode()
        return packet.value
    }

    val day = 16
    val title = "Packet Decoder"

    val input = readInput(day)

    println("--- Day $day: $title ---")

    check(part1(listOf("8A004A801A8002F478")) == 16)
    check(part1(listOf("620080001611562C8802118E34")) == 12)
    check(part1(listOf("C0015000016115A2E0802F182340")) == 23)
    check(part1(listOf("A0016C880162017C3686B18A3D4780")) == 31)
    print("Part One: "); println(part1(input))

    check(part2(listOf("C200B40A82")) == 3L)
    check(part2(listOf("04005AC33890")) == 54L)
    check(part2(listOf("880086C3E88112")) == 7L)
    check(part2(listOf("CE00C43D881120")) == 9L)
    check(part2(listOf("D8005AC2A8F0")) == 1L)
    check(part2(listOf("F600BC2D8F")) == 0L)
    check(part2(listOf("9C005AC2F8F0")) == 0L)
    check(part2(listOf("9C0141080250320F1802104A08")) == 1L)
    print("Part Two: "); println(part2(input))
}
