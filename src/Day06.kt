fun main() {

    fun part1(input: List<String>): Int {
        val (timeList, distanceList) = input.map { line ->
            line.substringAfter(':').split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        }

        return timeList.foldIndexed(1) { index, acc, time ->
            val minHold = (0..time).find { it * (time - it) > distanceList[index] }
            minHold?.let { acc * (time - minHold * 2 + 1) } ?: 0
        }
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = input.map { line -> line.filter { it.isDigit() }.toLong() }
        val minHold = (0..time).find { it * (time - it) > distance }
        return (minHold?.let { (time - minHold * 2 + 1) }) ?: 0
    }

    val testInput = readInput("Day06_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
