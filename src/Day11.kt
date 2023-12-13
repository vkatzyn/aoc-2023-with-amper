import kotlin.math.abs

fun main() {

    data class Position(val X: Long, val Y: Long)

    data class Galaxy(val position: Position)
    
    data class Space(val position: Position, val length: Long)
    
    data class GalaxyPath(val galaxy1: Galaxy,
                          val galaxy2: Galaxy,
                          val shortestPath: Long = abs(galaxy1.position.X - galaxy2.position.X) + abs(galaxy1.position.Y - galaxy2.position.Y)
    )
    
    data class Universe(val galaxies: List<Galaxy>, val spaces: List<Space>)

    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }
    
    fun part2(input: List<String>): Long {
        val temp = buildList {
            input.forEach {
                val charList = it.toCharArray().asList()
                if (!charList.contains('#')) {
                    val firstChar = 'S'
                    val list = mutableListOf(firstChar)
                    list.addAll(charList.subList(1, charList.size))
                    add(list)
                } else {
                    add(charList)
                }
            }
        }.transpose()

        val expanded = buildList {
            temp.forEach {
                val charList = it.toCharArray().asList()
                if (!charList.contains('#')) {
                    val list = mutableListOf<Char>()
                    list.addAll(charList.map { c -> if (c == 'S') '$' else 'C' })
                    add(list)
//                    add(charList.map { c -> if (c == 'S') '$' else 'C' }) // S – vertical (+Y), C – horizontal (+X), $ – both (+X, +Y), 
                } else {
                    add(charList)
                }
            }
        }.transpose()
        
//        expanded.forEach { it.println() }
        
        val spaceExpand: Long = 999999
        var currentI: Long = 0
        var currentJ: Long = 0
        var expandedI: Long = 0
        var expandedJ: Long = 0
        val galaxies = mutableListOf<Galaxy>()
        expanded.forEachIndexed { iIndex, chars ->
            currentI = iIndex.toLong() + expandedI
            expandedJ = 0
            chars.forEachIndexed { jIndex, c ->
                currentJ = jIndex.toLong() + expandedJ
                when (c) {
                    '#' -> galaxies.add(Galaxy(Position(currentI, currentJ)))
                    'S' -> expandedI += spaceExpand
                    'C' -> expandedJ += spaceExpand
                    '$' -> {
                        expandedI += spaceExpand
                        expandedJ += spaceExpand
                    }
                }
            }
        }
        
        return galaxies.sumOf { galaxy ->
            galaxies.sumOf { it ->
                if (it == galaxy) 0 else
                    GalaxyPath(galaxy, it).shortestPath
            }
        } / 2
    }

    fun part1(input: List<String>): Long {
        val temp = buildList {
            input.forEach {
                val charList = it.toCharArray().asList()
                add(charList)
                if (!charList.contains('#')) {
                    add(charList)
                }
            }
        }.transpose()

        val expanded = buildList {
            temp.forEach {
                val charList = it.toCharArray().asList()
                add(charList)
                if (!charList.contains('#')) {
                    add(charList)
                }
            }
        }.transpose()

        val galaxies = buildList {
            expanded.forEachIndexed { iIndex, charList ->
                charList.forEachIndexed { jIndex, char ->
                    if (char == '#') {
                        add(Galaxy(Position(iIndex.toLong(), jIndex.toLong())))
                    }
                }
            }
        }

        return galaxies.sumOf { galaxy ->
            galaxies.sumOf { it ->
                if (it == galaxy) 0 else
                    GalaxyPath(galaxy, it).shortestPath
            }
        } / 2


    }



    val testInput = readInput("Day11_test")
    part1(testInput).println()
    part2(testInput).println()
//    check(part1(testInput) == )
//    check(part2(testInput) == )

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
