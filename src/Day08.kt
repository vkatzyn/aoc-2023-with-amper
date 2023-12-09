import kotlin.math.abs

fun main() {

    data class Node(val name: String, val left: String, val right: String, var wayMap: Map<String, String>)

    fun parseAllSubWays(nodeName: String, instruction: String, network: Map<String, Node>): Map<String, String> {
        val subWayMap = mutableMapOf<String, String>()
        var currentNodeName = nodeName
        for (i in instruction.indices) {
            val subWay = instruction.substring(0, i + 1)
            val j = subWay.last()
            val nextNode = if (j == 'L')
                network[currentNodeName]?.left ?: "hmm1".also { it.println() }
            else
                network[currentNodeName]?.right ?: "hmm2".also { it.println() }
            subWayMap[subWay] = nextNode
            currentNodeName = nextNode
        }
        return subWayMap
    }

    fun parseNodes(input: List<String>): Map<String, Node> {
        val nodeStrList = input.subList(2, input.size)
        val network = mutableMapOf<String, Node>()
        nodeStrList.forEach {
            val name = it.substring(0, 3)
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)
            network[name] = Node(name, left, right, mutableMapOf())
        }
        network.keys.forEach {
            network[it]?.wayMap = parseAllSubWays(it, input[0], network)
        }
        return network
    }

        fun part1(input: List<String>): Int {
            val instructions = input[0]
            val network = parseNodes(input.subList(2, input.size))
    
            var reachedZ = false
            var i = 0
            var total = 0
            var currentNode = "AAA"
            while (!reachedZ) {
                if (i == instructions.length) i = 0
                val currentInstruction = instructions[i]
                if (currentInstruction == 'L') {
                    currentNode = network[currentNode]?.left ?: "AAA".also { println("oops1") }
                } else {
                    currentNode = network[currentNode]?.right ?: "BBB".also { println("oops2") }
                }
                if (currentNode == "ZZZ") {
                    reachedZ = true
                }
                i++
                total++
            }
    
            return total
        }
    
    fun gcd(a: Long, b: Long): Long {
        var result = minOf(a, b)
        while (result > 0) {
            if (a % result == 0L && b % result == 0L) {
                break;
            }
            result--;
        }
        return result
    }

    fun lcm(a: Long, b: Long): Long = abs(a * b)/gcd(a, b)

    fun part2lcm(input: List<String>): Long {
        val instructions = input[0]
        val nameToNodeMap = parseNodes(input)
        val counts = nameToNodeMap.keys.filter { it.endsWith('A') }.map { startingNode ->
            var current = startingNode
            var nodeStepsTotalCount = 0L
            while (!current.endsWith('Z')) {
                instructions.forEach {
                    current =
                            if (it == 'L') nameToNodeMap[current]?.left ?: "what".also { it.println() }
                            else nameToNodeMap[current]?.right ?: "whot".also { it.println() }
                }
                nodeStepsTotalCount += instructions.length
            }
            nodeStepsTotalCount
        }
        return counts.reduce { acc, l -> lcm(acc, l) }
    }

    val testInput = readInput("Day08_test")
    part1(testInput).println()
    part2lcm(testInput).println()
    check(part1(testInput) == 6)
    check(part2lcm(testInput) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2lcm(input).println()
}
