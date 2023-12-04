import kotlin.math.pow

fun main() {

    data class Card(val winningNumbers: List<String>,
                    val myNumbers: List<String>,
                    val winningNumbersCount: Int = myNumbers.count { it in winningNumbers },
                    var amount: Int = 1)

    fun getCards(input: List<String>) = input.map { line ->
        val (winningNumbers, myNumbers) = line
                .split(':')[1].trim()
                .split('|')
                .map { it.trim().split(' ').filter(String::isNotEmpty) }
        Card(winningNumbers, myNumbers)
    }

    fun part1(input: List<String>): Int {
        return getCards(input).sumOf { 2.0.pow(it.winningNumbersCount - 1).toInt() }
    }

    fun part2(input: List<String>): Int {
        val cards = getCards(input)
        var result = 0
        cards.forEachIndexed { index, card ->
            result += card.amount
            for (i in index + 1..index + card.winningNumbersCount) cards[i].amount += card.amount
        }
        return result
    }

    val testInput = readInput("Day04_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
