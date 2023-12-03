fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOf { lines ->
            val first = lines.first { it.isDigit() }.digitToInt()
            val second = lines.last { it.isDigit() }.digitToInt()
            "$first$second".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val list: ArrayList<String> = arrayListOf()
        input.forEach { inputStr ->
            var str: String
            str = inputStr.replace("one".toRegex(), "o1e")
            str = str.replace("two".toRegex(), "t2o")
            str = str.replace("three".toRegex(), "th3ee")
            str = str.replace("four".toRegex(), "f4ur")
            str = str.replace("five".toRegex(), "f5ve")
            str = str.replace("six".toRegex(), "s6x")
            str = str.replace("seven".toRegex(), "se7en")
            str = str.replace("eight".toRegex(), "ei8ht")
            str = str.replace("nine".toRegex(), "n9ne")
            list.add(str)
        }
        return part1(list)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println(part2(testInput))
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
