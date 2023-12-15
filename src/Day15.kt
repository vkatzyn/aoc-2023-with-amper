fun main() {

    fun hash(s: String): Int {
        var cur: Int = 0
        for (c in s) {
            cur += c.code
            cur *= 17
            cur %= 256
        }
        return cur
    }

    data class Lens(val label: String, val focalLength: Int)

    fun part1(input: String): Int {
        val sequence = input.split(',')
        return sequence.sumOf { hash(it) }
    }

    fun part2(input: String): Int {
        val sequence = input.split(',')
        val boxes = List(256) { mutableListOf<Lens>() }
        for (s in sequence) {
            val op = if ('=' in s) '=' else '-'
            val label = s.substringBefore(op)
            val focalLength = if (op == '=') s.last().digitToInt() else 0
            val boxNum = hash(label)
            val lens = Lens(label, focalLength)
            when (op) {
                '=' -> {
                    val index = boxes[boxNum].indexOfFirst { it.label == lens.label }
                    if (index != -1) boxes[boxNum][index] = lens else boxes[boxNum] += lens
                }

                '-' -> {
                    boxes[boxNum].removeIf { it.label == lens.label }
                }
            }
        }
        return boxes.flatMapIndexed { boxIndex: Int, lensList: MutableList<Lens> ->
            boxes[boxIndex].mapIndexed { lensIndex, lens -> (boxIndex + 1) * (lensIndex + 1) * lensList[lensIndex].focalLength }
        }.sum()
    }


    val testInput = readInput("Day15_test")[0]
    part1(testInput).println()
    part2(testInput).println()
//    check(part1(testInput) == )
//    check(part2(testInput) == )

    val input = readInput("Day15")[0]
    part1(input).println()
    part2(input).println()
}
