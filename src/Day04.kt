import kotlin.math.pow
import kotlin.math.roundToInt

fun main() {
    data class Card(val winningNumbers: List<String>, val myNumbers: List<String>, var amount: Int)

    fun getCards(input: List<String>) = input.map {
        val nums = it.split(':')[1].trim().split('|')
        Card(nums[0].trim().split(' '),
                nums[1].trim().split(' ').filter { num -> num.isNotEmpty() },
                1)
    }

    fun Card.getWinningNumbersCount() = myNumbers.filter { num -> num in winningNumbers }.count()

    fun part1(input: List<String>): Int {
        val cards = getCards(input)
        return cards.sumOf {
            2.0.pow(it.getWinningNumbersCount() - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val cards = getCards(input)
        cards.forEachIndexed { index, card ->
            val winningNumbersCount = card.getWinningNumbersCount()
            for (i in index + 1..index + winningNumbersCount) {
                cards[i].amount += card.amount
            }
        }
        return cards.sumOf { it.amount }
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
