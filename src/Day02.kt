import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.math.max

fun main() {

    fun isPossible(pulls: List<String>): Boolean {
        val colorsMaximums = mapOf("red" to 12, "green" to 13, "blue" to 14)
        return pulls.none {
            val num = it.split(' ')[0].toInt()
            val color = it.split(' ')[1]
            colorsMaximums[color]!! < num
        }
    }

    fun getRealColorsMaximums(pulls: List<String>): Map<String, Int> {
        val maximums = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
        pulls.forEach {
            val num = it.split(" ")[0].toInt()
            val color = it.split(" ")[1]
            maximums[color] = max(maximums[color]!!, num)
        }
        return maximums
    }

    fun part1(input: List<String>): Int =
            input.sumOf { line ->
                val (gameIdStr, game) = line.split(": ")
                val gameId = gameIdStr.split(" ")[1]
                if (isPossible(game.split("; ", ", "))) {
                    gameId.toInt()
                } else {
                    0
                }
            }

    fun part2(input: List<String>): Int =
            input.sumOf { line ->
                val game = line.split(": ")[1]
                val pulls = game.split("; ", ", ")
                getRealColorsMaximums(pulls).values.reduce(Int::times)
            }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()

    application {
        Window(onCloseRequest = { this.exitApplication() }) {
            Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                Text(text = part1(input).toString(), fontSize = 24.sp)
                Text(text = part2(input).toString(), fontSize = 24.sp)
            }
        }
    }
}
