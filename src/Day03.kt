data class NumberData(val lineNum: Int, val startIndex: Int, val endIndex: Int, val number: Int)

fun main() {
    
    
    
    
    
    ///////////////

    fun getNumberDataList(input: List<String>): List<NumberData> {
        val resultList = arrayListOf<NumberData>()
        input.forEachIndexed { lineIndex, line ->
            var isProcessingNumber = false;
            var startIndex = -1
            var endIndex = -1
            line.forEachIndexed { index, c ->
                if (c.isDigit()) {
                    if (isProcessingNumber) {
                        endIndex = index
                    } else {
                        isProcessingNumber = true
                        startIndex = index
                    }
                } else {
                    if (isProcessingNumber) {
                        isProcessingNumber = false
                        endIndex = if (endIndex == -1) startIndex else endIndex
                        val num = line.substring(startIndex, endIndex + 1).toInt()
                        resultList.add(NumberData(lineIndex, startIndex, endIndex, num))
                        startIndex = -1
                        endIndex = -1
                    }
                }
                if (line.length - 1 == index && startIndex != -1) {
                    endIndex = if (endIndex == -1) startIndex else endIndex
                    val num = line.substring(startIndex, endIndex + 1).toInt()
                    resultList.add(NumberData(lineIndex, startIndex, endIndex, num))
                }
            }
        }
        return resultList
    }


    fun part1(input: List<String>): Int {
        val numberDataList = getNumberDataList(input)
        val lineLength = input[0].length
        var result = 0
        numberDataList.forEach {
            var adjacentSymbols: String = ""
            with(it) {
                val isStartIndexFirst = startIndex == 0
                val isEndIndexLast = endIndex == lineLength - 1
                if (!isStartIndexFirst) adjacentSymbols += input[lineNum][startIndex - 1]
                if (!isEndIndexLast) adjacentSymbols += input[lineNum][endIndex + 1]
                if (lineNum != 0) {
                    adjacentSymbols += input[lineNum - 1]
                            .substring(if (isStartIndexFirst) startIndex else startIndex - 1,
                                    if (isEndIndexLast) endIndex + 1 else endIndex + 2)
                }
                if (lineNum != input.size - 1) {
                    adjacentSymbols += input[lineNum + 1]
                            .substring(if (isStartIndexFirst) startIndex else startIndex - 1,
                                    if (isEndIndexLast) endIndex + 1 else endIndex + 2)
                }
                if (adjacentSymbols.any { !it.isDigit() && !it.equals('.') }) {
                    result += number
                }
            }
        }
        return result
    }

    data class GearData(val lineNum: Int, val index: Int, val number: Int, val numberX: Int, val numberY: Int)

    fun part2(input: List<String>): Int {
        val numberDataList = getNumberDataList(input)
        val gearDataList = arrayListOf<GearData>()
        val lineLength = input[0].length
        var result = 0
        numberDataList.forEach {
            var adjacentSymbols: String = ""
            with(it) {
                val isStartIndexFirst = startIndex == 0
                val isEndIndexLast = endIndex == lineLength - 1
                if (!isStartIndexFirst && input[lineNum][startIndex - 1] == '*') {
                    gearDataList.add(GearData(lineNum, startIndex - 1, number, startIndex, lineNum))
                }
                if (!isEndIndexLast && input[lineNum][endIndex + 1] == '*') {
                    gearDataList.add(GearData(lineNum, endIndex + 1, number, startIndex, lineNum))
                }
                if (lineNum != 0) {
                    input[lineNum - 1].forEachIndexed { index, c ->
                        if (index in (if (isStartIndexFirst) startIndex else startIndex - 1)
                                ..(if (isEndIndexLast) endIndex else endIndex + 1)
                                && c == '*') {
                            gearDataList.add(GearData(lineNum - 1, index, number, startIndex, lineNum))
                        }
                    }
                }
                if (lineNum != input.size - 1) {
                    input[lineNum + 1].forEachIndexed { index, c ->
                        if (index in (if (isStartIndexFirst) startIndex else startIndex - 1)
                                ..(if (isEndIndexLast) endIndex else endIndex + 1)
                                && c == '*') {
                            gearDataList.add(GearData(lineNum + 1, index, number, startIndex, lineNum))
                        }
                    }
                }

            }
        }
        val usedGearsIndices = arrayListOf<Int>()
        gearDataList.forEach { it1 ->
            var ind: Int = 0
            val list = gearDataList.filterIndexed { index, it2 ->
                if (it1.index == it2.index
                        && it1.lineNum == it2.lineNum
                        && (it1.numberX != it2.numberX || it1.numberY != it2.numberY)
                        && index !in usedGearsIndices) {
                    ind = index
                    return@filterIndexed true
                } else {
                    return@filterIndexed false
                }
            }
            if (list.size == 1) {
                result += it1.number * list[0].number
                usedGearsIndices.add(ind)
            }
        }
        return result / 2
    }

    val testInput = readInput("Day03_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()

}
