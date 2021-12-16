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

    class Packet {
        var version: Int = 0
        var typeID: Int = 0
        var value: Long = 0
        var subPackets: MutableList<Packet> = mutableListOf()

        override fun toString(): String {
            return "Packet v:$version t:$typeID - ${value}"
        }
    }

    class PacketDecoder(val br: BitReader) {
        fun decodeValue(packet: Packet) {
            var bits = ""
            do {
                val hasMore = br.read() == 1
                bits += br.read(4)
            } while (hasMore)
            packet.value = bits.toLong(2)
        }

        fun decodeOperator(packet: Packet) {
            val lengthTypeID = br.read()
            if (lengthTypeID == 0) {
                val length = br.readInt(15)
                val p = br.p
                while (br.p - p < length) {
                    val sub = Packet()
                    packet.subPackets.add(sub)
                    decode(sub)
                }
            } else {
                val length = br.readInt(11)
                repeat(length) {
                    val sub = Packet()
                    packet.subPackets.add(sub)
                    decode(sub)
                }
            }
        }

        fun decode(packet: Packet) {
            packet.version = br.readInt(3)
            packet.typeID = br.readInt(3)
            if (packet.typeID == 4) {
                decodeValue(packet)
                return
            }
            decodeOperator(packet)
            val subValues = packet.subPackets.map { it.value }
            when (packet.typeID) {
                0 -> packet.value = subValues.sumOf { it }
                1 -> packet.value = subValues.reduce { acc, l -> acc * l }
                2 -> packet.value = subValues.minOrNull() ?: 0
                3 -> packet.value = subValues.maxOrNull() ?: 0
                5 -> packet.value = subValues.let { (a, b) -> if (a > b) 1 else 0 }
                6 -> packet.value = subValues.let { (a, b) -> if (a < b) 1 else 0 }
                7 -> packet.value = subValues.let { (a, b) -> if (a == b) 1 else 0 }
            }
        }

        fun decode(): Packet {
            val packet = Packet()
            decode(packet)
            return packet
        }
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)
        val reader = BitReader(parsedInput)
        val decoder = PacketDecoder(reader)
        val packet = decoder.decode()

        fun sumOfVersion(packet: Packet): Int {
            var sum = packet.version
            packet.subPackets.forEach {
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
