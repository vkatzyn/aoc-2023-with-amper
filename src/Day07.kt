fun main() {

    data class Hand(val cards: String, val cardsSorted: String, val bid: Int, val type: Int)

    fun part1(input: List<String>): Int {
        val cardLabels = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').reversed()
        val cardStrengths = cardLabels.associateWith { cardLabels.indexOf(it) }

        fun getHandType(cards: String): Int {
            val cardsRemovedDuplicates = cards.toSet()
            val list = cardsRemovedDuplicates.map { label ->
                cards.count { it == label }
            }.sortedDescending()
            val am1 = list[0]
            val am2 = if (list.size > 1) list[1] else 0
            return if (am1 == 5) 7 else if (am1 == 4) 6 else if (am1 == 3 && am2 == 2) 5 else if (am1 == 3) 4 else if (am1 == 2 && am2 == 2) 3 else if (am1 == 2) 2 else 1
        }

        fun parseHands(input: List<String>): List<Hand> {
            return input.map { it ->
                val (cards, bid) = it.split(' ')
                val cardsArr = cards.toCharArray().sortedWith(
                        compareBy<Char> { it.isDigit() }.thenBy { it }
                )
                val cardsSorted = cardsArr.joinToString("")
                val type = getHandType(cardsSorted)
                Hand(cards, cardsSorted, bid.toInt(), type)
            }
        }

        fun compareTwoHands(a: Hand, b: Hand): Int {
            if (a.type != b.type) {
                return compareValues(a.type, b.type)
            } else {
                for (i in 0..4) {
                    if (a.cards[i] == b.cards[i]) continue
                    return compareValues(cardStrengths[a.cards[i]], cardStrengths[b.cards[i]])
                }
                return 0
            }
        }

        val handComparator = Comparator<Hand> { hand1, hand2 -> compareTwoHands(hand1, hand2) }
        val hands = parseHands(input).sortedWith(handComparator)
        return hands.foldIndexed(0) { index, acc, hand ->
            acc + hand.bid * (index + 1)
        }
    }

    fun part2(input: List<String>): Int {
        val cardLabels =
                listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J').reversed()
        val cardStrengths = cardLabels.associateWith { cardLabels.indexOf(it) }
        fun getHandType(cards: String): Int {
            val cardsRemovedDuplicates = cards.toSet()
            val list = cardsRemovedDuplicates.map { label ->
                cards.count { label != 'J' && it == label }
            }.sortedDescending()
            val am1 = list[0] + cards.count { it == 'J' }
            val am2 = if (list.size > 1) list[1] else 0
            return if (am1 == 5) 7 else if (am1 == 4) 6 else if (am1 == 3 && am2 == 2) 5 else if (am1 == 3) 4 else if (am1 == 2 && am2 == 2) 3 else if (am1 == 2) 2 else 1
        }

        fun parseHands(input: List<String>): List<Hand> {
            return input.map { it ->
                val (cards, bid) = it.split(' ')
                val cardsArr = cards.toCharArray().sortedWith(
                        compareBy<Char> { it.isDigit() }.thenBy { it }
                )
                val cardsSorted = cardsArr.joinToString("")
                val type = getHandType(cardsSorted)
                Hand(cards, cardsSorted, bid.toInt(), type)
            }
        }

        fun compareTwoHands(a: Hand, b: Hand): Int {
            if (a.type != b.type) {
                return compareValues(a.type, b.type)
            } else {
                for (i in 0..4) {
                    if (a.cards[i] == b.cards[i]) continue
                    return compareValues(cardStrengths[a.cards[i]], cardStrengths[b.cards[i]])
                }
                return 0
            }
        }


        val handComparator = Comparator<Hand> { hand1, hand2 -> compareTwoHands(hand1, hand2) }
        val hands = parseHands(input).sortedWith(handComparator)
        return hands.foldIndexed(0) { index, acc, hand ->
            acc + hand.bid * (index + 1)
        }
    }

    val testInput = readInput("Day07_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
