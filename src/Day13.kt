sealed class Pattern(val sequences: List<String>)

data class VerticalPattern(val rows: List<String>) : Pattern(rows) // *100
data class HorizontalPattern(val columns: List<String>) : Pattern(columns)

fun main() {

    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }

    fun String.hammingDistance(other: String): Int =
            foldIndexed(0) { index, acc, c -> acc + if (other[index] != c) 1 else 0 }

    fun Pattern.getReflectedCount(hammingDistance: Int): Long {
        val counts = mutableListOf<Long>()
        for (i in 1..<sequences.size) {
            if (sequences[i] == sequences[i - 1] || sequences[i].hammingDistance(sequences[i - 1]) == 1) {
                val less = minOf(i, sequences.size - i)
                val upper = sequences.subList(i - less, i)
                val lower = sequences.subList(i, i + less).reversed()
                val hammingDistanceSum = upper.foldIndexed(0) { index, acc, s -> acc + s.hammingDistance(lower[index]) }
                if (hammingDistanceSum == hammingDistance) {
                    counts.add(if (this is VerticalPattern) i * 100L else i + 0L)
                }
            }
        }
        return counts.maxOrNull() ?: 0
    }

    fun createPattern(strings: List<String>, vertical: Boolean): Pattern =
            if (vertical) {
                VerticalPattern(strings)
            } else {
                HorizontalPattern(strings
                        .map { s -> s.toList() }
                        .transpose()
                        .map { chars -> chars.joinToString("") })
            }

    fun getPatterns(input: List<String>, vertical: Boolean): List<Pattern> {
        val patterns = buildList {
            var temp = mutableListOf<String>()
            input.forEach {
                if (it.isNotBlank()) {
                    temp.add(it)
                } else {
                    add(createPattern(temp, vertical))
                    temp = mutableListOf<String>()
                }
            }
            add(createPattern(temp, vertical))
        }
        return patterns
    }

    fun part1(input: List<String>): Long {
        val verticalPatterns = getPatterns(input, true)
        val horizontalPatterns = getPatterns(input, false)
        return horizontalPatterns.sumOf { it.getReflectedCount(0) } +
                verticalPatterns.sumOf { it.getReflectedCount(0) }
    }

    fun part2(input: List<String>): Long {
        val verticalPatterns = getPatterns(input, true)
        val horizontalPatterns = getPatterns(input, false)
        return horizontalPatterns.sumOf { it.getReflectedCount(1) } +
                verticalPatterns.sumOf { it.getReflectedCount(1) }
    }

    val testInput = readInput("Day13_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 405L)
    check(part2(testInput) == 400L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
