fun main() {

    fun findNextValue(values: List<Int>): Int {
        var nextValue = values.last()
        var allZeroes = false
        var currentSequence = mutableListOf<Int>()
        currentSequence.addAll(values)
        while (!allZeroes) {
            val sequenceOfDifferences = mutableListOf<Int>()
            for (i in 1..currentSequence.indices.last) {
                sequenceOfDifferences.add(currentSequence[i] - currentSequence[i - 1])
            }
            nextValue += sequenceOfDifferences[sequenceOfDifferences.lastIndex]
            allZeroes = sequenceOfDifferences.all { it == 0 }
            currentSequence = sequenceOfDifferences
        }
        return nextValue
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
