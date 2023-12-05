fun main() {

    fun part1(input: List<String>): Long {
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }
        var readingMap = false

        val currentMap = mutableMapOf<Long, Long>()
        var currentValues = seeds
        for (i in 2..input.indices.last) {
            val line = input[i]
            if ("-to-" in line) {
                readingMap = true
                continue
            } 
            if (line.isBlank()) {
                readingMap = false
                currentValues = currentValues.map { currentMap[it] ?: it }
                currentMap.clear()
            }
            if (readingMap) {
                // destRangeStart, sourceRangeStart, rangeLength
                val (destRangeStart, sourceRangeStart, rangeLength) = line.split(' ').map { it.toLong() }
                for (j in currentValues.indices) {
                    if (currentValues[j] in sourceRangeStart..<sourceRangeStart + rangeLength) {
                        currentMap[currentValues[j]] = (currentValues[j] - sourceRangeStart + destRangeStart)
                    }
                }
            }
        }
        currentValues = currentValues.map { currentMap[it] ?: it }
        currentMap.clear()
        return currentValues.min()
    }

    fun getSeedRanges(input: List<String>): List<LongRange> {
        val seedList = input[0].split(": ")[1].split(" ").map { it.toLong() }
        val seedRanges = mutableListOf<LongRange>()
        for (i in seedList.indices step 2) {
            seedRanges.add(seedList[i]..<seedList[i] + seedList[i + 1])
        }
        return seedRanges
    }

    fun getMappingsList(input: List<String>): List<List<String>> {
        val mappings: MutableList<List<String>> = mutableListOf()
        var currentMappingList: MutableList<String> = mutableListOf()
        var isMapping = false
        for (i in 2..input.indices.last) {
            val line = input[i]
            if ("-to-" in line) {
                isMapping = true
                continue
            } else if (line.isBlank()) {
                isMapping = false
                mappings += currentMappingList
                currentMappingList = mutableListOf()
            } else {
                currentMappingList += line
            }
        }
        if (isMapping) {
            mappings += currentMappingList
            currentMappingList = mutableListOf()
        }
        return mappings
    }

    fun part2(input: List<String>): Long {
        val seedRanges = getSeedRanges(input)
        val mappings = getMappingsList(input)

        val mappedRanges = mutableListOf<LongRange>()
        val notMappedRanges = mutableListOf<LongRange>()
        notMappedRanges.addAll(seedRanges)

        for (mapping in mappings) {
            for (mapLine in mapping) {
                val (destRangeStart, sourceRangeStart, rangeLength) =
                        mapLine.split(' ').map { it.toLong() }
                val mappingRange = sourceRangeStart..<sourceRangeStart + rangeLength
                val diff = destRangeStart - sourceRangeStart


                val notIntersectedRanges: MutableList<LongRange> = mutableListOf()
                for (seedRange in notMappedRanges) {
                    var intersectionRange: LongRange? = null

                    seedRange.let {
                        if (mappingRange.first >= it.first && mappingRange.last <= it.last) {
                            // внутри
                            intersectionRange = mappingRange.first..mappingRange.last
                        } else if (mappingRange.first >= it.first && mappingRange.last > it.last && mappingRange.first <= it.last) {
                            intersectionRange = mappingRange.first..it.last
                            //внутри, но mappingRange впереди
                        } else if (mappingRange.first < it.first && mappingRange.last <= it.last && it.first <= mappingRange.last) {
                            intersectionRange = it.first..mappingRange.last
                            // внутри, но mappingRange позади
                        } else if (mappingRange.first <= it.first && mappingRange.last >= it.last) {
                            intersectionRange = it.first..it.last
                            // не внутри
                        }
                    }



                    intersectionRange?.let {
                        if (it.first == seedRange.first && it.last == seedRange.last) {
                            return@let
                        }
                        val isIntersectionInSeed = it.first >= seedRange.first && it.last <= seedRange.last
                        if (isIntersectionInSeed) {
                            if (seedRange.first != it.first) {
                                notIntersectedRanges.add(seedRange.first..<it.first)
                            }
                            if (seedRange.last != it.last) {
                                notIntersectedRanges.add((it.last + 1)..seedRange.last)
                            }
                        } else {
                            if (it.first > seedRange.first) { // то it.last >= seedRange.last
                                notIntersectedRanges.add((it.last + 1)..seedRange.last)
                            } else { // то it.last < seedRange.last
                                notIntersectedRanges.add(seedRange.first..<it.first)
                            }
                        }
                    }

                    if (intersectionRange != null) {
                        val mappedRange = intersectionRange?.let { it.first() + diff..it.last() + diff }
                        mappedRanges.add(mappedRange!!)
                    } else {
                        notIntersectedRanges.add(seedRange)
                    }
                }
                notMappedRanges.clear()
                notMappedRanges.addAll(notIntersectedRanges)
            }
            notMappedRanges.addAll(mappedRanges)
            mappedRanges.clear()
        }
        return notMappedRanges.minOf { it.min() }
    }

    val testInput = readInput("Day05_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
