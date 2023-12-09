fun main() {

    fun findNextValue(values: List<Int>): Int {
        val differenceSequences =
                generateSequence(values) {
                    it.windowed(2) { (a, b) -> b - a }
                }.takeWhile { it.any { value -> value != 0 } }
        return differenceSequences.sumOf { it.last() }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val values = line.split(' ').map { it.toInt() }
            findNextValue(values)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val values = line.split(' ').map { it.toInt() }.reversed()
            findNextValue(values)
        }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput).also { it.println() } == 114)
    check(part2(testInput).also { it.println() } == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
