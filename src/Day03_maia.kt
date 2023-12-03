sealed class Element
data class Number(val value: Int, val xRange: IntRange, val row: Int) : Element() {
    val expandedColumn = xRange.first - 1 .. xRange.last + 1
    val expandedRow = row - 1 .. row + 1
}

fun Number(number: String, start: Int, end: Int, row: Int): Number = Number(number.toInt(), start .. end, row)

data class Symbol(val value: Char, val column: Int, val row: Int) : Element()

typealias SchematicRow = List<Element>

fun extractElements(input: String, row: Int): List<Element> = buildList {
    var numberStart = -1
    var currentNumber = ""
    for ((index, c) in input.withIndex()) {
        when {
            (c.isDigit()) -> {
                currentNumber += c
                if (numberStart == -1) numberStart = index
            }
            
            else -> {
                if (c != '.') this.add(Symbol(value = c, column = index, row = row))
                if (currentNumber.isNotEmpty()) {
                    this.add(Number(number = currentNumber, start = numberStart, end = index - 1, row = row))
                    currentNumber = ""
                    numberStart = -1
                }
            }
        }
    }
    if (currentNumber.isNotEmpty()) {
        this.add(Number(number = currentNumber, start = numberStart, end = input.length - 1, row = row))
    }
}
private fun List<SchematicRow>.findParts(): Set<Number> {
    val parts = mutableSetOf<Number>()
    this.windowed(2).map { twoRows ->
        val symbols: List<Symbol> = twoRows.flatten().filterIsInstance<Symbol>()
        val numbers: List<Number> = twoRows.flatten().filterIsInstance<Number>()
        numbers.filter { n ->
            symbols.any { s ->
                s.column in n.expandedColumn
            }
        }.forEach { parts.add(it) }
    }
    return parts
}

private fun List<Element>.findGearParts(): List<Pair<Int, Int>> {
    // ...
    val parts = this.filterIsInstance<Number>()
    val potentialGears = this.filterIsInstance<Symbol>().filter { it.value == '*' }
    return potentialGears
        .map { s ->
             // list of neighbours
            parts.filter { (s.row in it.expandedRow) && (s.column in it.expandedColumn) }
        }
        .filter {
            it.size == 2
        }
        .map {
            it[0].value to it[1].value
        }
}

fun main() {

    fun part1(input: List<String>): Int {
        val engineSchematic: List<SchematicRow> = input.mapIndexed { i, s -> extractElements(s, i) }
        return engineSchematic
            .findParts()
            .sumOf {
                it.value
            }
    }
    
    fun part2(input: List<String>): Int {
        val engineSchematic: List<SchematicRow> = input.mapIndexed { i, s -> extractElements(s, i) }
        return engineSchematic
            .flatten()
            .findGearParts()
            .sumOf { parts -> parts.first * parts.second }
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
