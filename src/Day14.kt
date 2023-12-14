fun main() {

    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }

    fun countRockLoadsInColumns(input: List<String>): List<Int> {
        val counts = mutableListOf<Int>()
        for (j in input[0].indices) {
            var colRockCount = 0
            for (i in input.indices) {
                if (input[i][j] == 'O') {
                    colRockCount += input[i].length - i
                }
            }
            counts.add(colRockCount)
        }
        return counts
    }

    fun String.splitKeeping(str: String): List<String> {
        return this.split(str).flatMap { listOf(it, str) }.dropLast(1).filterNot { it.isEmpty() }
    }

    fun sortDish(input: List<String>, reversed: Boolean): List<String> {
        val result = mutableListOf<String>()
        for (i in input.indices) {
            val cur = input[i]
            var curSorted = ""
            val splitted = cur.splitKeeping("#")
            for (ind in splitted.indices) {
                val sub = splitted[ind]
                if (sub == "#") {
                    curSorted += "#"
                } else {
                    val oCount = sub.count { it == 'O' }
                    val dotCount = sub.length - oCount
                    val toAdd = if (reversed) {
                        ".".repeat(dotCount) + "O".repeat(oCount)
                    } else {
                        "O".repeat(oCount) + ".".repeat(dotCount)
                    }
                    curSorted += toAdd
                }
            }
            result.add(curSorted)
        }
        return result
    }

    fun tiltNorth(input: List<String>): List<String> {
        val transposedInput = input.map { it.toList() }.transpose().map { it.joinToString("") }
        val sortedTransposedInput = sortDish(transposedInput, false)
        val result = sortedTransposedInput.map { it.toList() }.transpose().map { it.joinToString("") }
        return result
    }

    fun tiltWest(input: List<String>): List<String> {
        val result = sortDish(input, false)
        return result
    }

    fun tiltSouth(input: List<String>): List<String> {
        val transposedInput = input.map { it.toList() }.transpose().map { it.joinToString("") }
        val sortedTransposedInput = sortDish(transposedInput, true)
        val result = sortedTransposedInput.map { it.toList() }.transpose().map { it.joinToString("") }
        return result
    }

    fun tiltEast(input: List<String>): List<String> {
        val result = sortDish(input, true)
        return result
    }

    fun part1(input: List<String>): Int {
        val sortedRocksInput = tiltNorth(input)
        val colRockCounts = countRockLoadsInColumns(sortedRocksInput)
        return colRockCounts.sum()
    }

    fun part2(input: List<String>): Int {
        var sortedRocksInput: List<String>? = input
        val hashes = mutableListOf<Int>()
        hashes.add(sortedRocksInput.hashCode())
        for (i in 1..1000000000) {
            sortedRocksInput = tiltEast(tiltSouth(tiltWest(tiltNorth(sortedRocksInput!!))))
            hashes.add(sortedRocksInput.hashCode())
            if (hashes.distinct().size < hashes.size) {
                break
            }
        }
        val firstOccurenceIndex = hashes.indexOf(hashes.last())
        val secondOccurenceIndex = firstOccurenceIndex + hashes.subList(firstOccurenceIndex + 1, hashes.size).indexOf(hashes.last())
        val cycleLength = secondOccurenceIndex - firstOccurenceIndex + 1
        val cyclePart = (1000000000 - firstOccurenceIndex) % cycleLength
        var currentHash = 0
        sortedRocksInput = input
        while (currentHash != hashes[firstOccurenceIndex + cyclePart]) {
            sortedRocksInput = tiltEast(tiltSouth(tiltWest(tiltNorth(sortedRocksInput!!))))
            currentHash = sortedRocksInput.hashCode()

        }
        val colRockCounts = countRockLoadsInColumns(sortedRocksInput!!)
        return colRockCounts.sum()
    }

    val testInput = readInput("Day14_test")
    part1(testInput).println()
    part2(testInput).println()
//    check(part1(testInput) == )
//    check(part2(testInput) == )

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
