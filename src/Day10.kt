enum class Direction {
    NORTH, SOUTH, WEST, EAST, NO_DIRECTION
}

fun main() {

    data class Tile(val symbol: Char,
                    var i: Int,
                    var j: Int,
                    var inCycle: Boolean = false,
                    var isOriginal: Boolean = true,
                    var isProcessing: Boolean = false,
                    var isEnclosed: Boolean = true,
                    var isProcessed: Boolean = false) {
        override fun equals(other: Any?): Boolean {
            if (other !is Tile) {
                return false
            }
            return symbol == other.symbol && i == other.i && j == other.j
        }
    }

    data class Maze(val tiles: List<List<Tile>>,
                    val topBound: Int = 0,
                    val leftBound: Int = 0,
                    val bottomBound: Int = tiles.size - 1,
                    val rightBound: Int = tiles[0].size - 1)

    fun getPossibleConnectedPipes(pipe: Char, direction: Direction): Set<Char> {
        val set = when (pipe) {
            '|' -> setOf('|', 'L', 'J', '7', 'F', 'S')
            '-' -> setOf('-', 'L', 'J', '7', 'F', 'S')
            'L' -> setOf('|', '-', 'J', '7', 'F', 'S')
            'J' -> setOf('|', '-', 'L', '7', 'F', 'S')
            '7' -> setOf('|', '-', 'L', 'J', 'F', 'S')
            'F' -> setOf('|', '-', 'L', 'J', '7', 'S')
            'S' -> setOf('|', '-', 'L', 'J', '7', 'F', 'S')
            else -> emptySet()
        }.toMutableSet()
        return when (direction) {
            Direction.NORTH -> if (pipe == '-' || pipe == '7' || pipe == 'F') emptySet() else set - setOf('-', 'L', 'J')
            Direction.SOUTH -> if (pipe == '-' || pipe == 'J' || pipe == 'L') emptySet() else set - setOf('-', 'F', '7')
            Direction.EAST -> if (pipe == '|' || pipe == 'J' || pipe == '7') emptySet() else set - setOf('|', 'L', 'F')
            Direction.WEST -> if (pipe == '|' || pipe == 'F' || pipe == 'L') emptySet() else set - setOf('|', 'J', '7')
            Direction.NO_DIRECTION -> emptySet()
        }
    }

    fun findNextPipes(maze: Maze, pipe: Tile, excludePipe: Tile?): List<Tile> {
        val adjacentPipes = mutableListOf<Tile>()
        if (pipe.i > maze.topBound) {
            val pipeCandidate = maze.tiles[pipe.i - 1][pipe.j]
            if (pipeCandidate.symbol in getPossibleConnectedPipes(pipe.symbol, Direction.NORTH) && pipeCandidate != excludePipe) adjacentPipes.add(pipeCandidate)
        }
        if (pipe.i < maze.bottomBound) {
            val pipeCandidate = maze.tiles[pipe.i + 1][pipe.j]
            if (pipeCandidate.symbol in getPossibleConnectedPipes(pipe.symbol, Direction.SOUTH) && pipeCandidate != excludePipe) adjacentPipes.add(pipeCandidate)
        }
        if (pipe.j > maze.leftBound) {
            val pipeCandidate = maze.tiles[pipe.i][pipe.j - 1]
            if (pipeCandidate.symbol in getPossibleConnectedPipes(pipe.symbol, Direction.WEST) && pipeCandidate != excludePipe) adjacentPipes.add(pipeCandidate)
        }
        if (pipe.j < maze.rightBound) {
            val pipeCandidate = maze.tiles[pipe.i][pipe.j + 1]
            if (pipeCandidate.symbol in getPossibleConnectedPipes(pipe.symbol, Direction.EAST) && pipeCandidate != excludePipe) adjacentPipes.add(pipeCandidate)
        }

        return adjacentPipes
    }

    fun Maze.markMainLoopTiles() {
        val firstCoordinate = tiles.indexOfFirst { it.any { c -> c.symbol == 'S' } }
        val firstPipe = tiles[firstCoordinate].first { it.symbol == 'S' }.apply { inCycle = true }
        val (firstConnectedPipeA, firstConnectedPipeB) = findNextPipes(this, firstPipe, null)

        var i = 1
        var foundPipe1 = firstConnectedPipeA.apply { inCycle = true }
        var foundPipe2 = firstConnectedPipeB.apply { inCycle = true }

        var excludePipe1 = firstPipe
        var excludePipe2 = firstPipe
        var nextExcludePipe1: Tile?
        var nextExcludePipe2: Tile?
        do {
            nextExcludePipe1 = foundPipe1
            nextExcludePipe2 = foundPipe2
            foundPipe1 = findNextPipes(this, foundPipe1, excludePipe1)[0].apply { inCycle = true }
            foundPipe2 = findNextPipes(this, foundPipe2, excludePipe2)[0].apply { inCycle = true }
            excludePipe1 = nextExcludePipe1
            excludePipe2 = nextExcludePipe2
            i++
        } while (foundPipe1 != foundPipe2)
    }

    fun part1(input: List<String>): Int {
        val mazeRaw = input.mapIndexed { index, str ->
            str.mapIndexed { indexC, c ->
                Tile(c, index, indexC)
            }
        }
        val maze = Maze(mazeRaw)
        maze.markMainLoopTiles()
        val mainLoopLength = maze.tiles.sumOf { tiles -> tiles.count { it.inCycle } }
        return mainLoopLength / 2
    }

    fun Tile.isConnectedTo(tile: Tile, direction: Direction): Boolean {
        return tile.symbol in getPossibleConnectedPipes(symbol, direction)
    }

    fun Tile.getDirectionTo(tile: Tile): Direction {
        return if (i - tile.i == -1) {
            Direction.SOUTH
        } else if (i - tile.i == 1) {
            Direction.NORTH
        } else if (j - tile.j == -1) {
            Direction.EAST
        } else if (j - tile.j == 1) {
            Direction.WEST
        } else {
            Direction.NO_DIRECTION
        }
    }

    fun Tile.isConnectedTo(tile: Tile): Boolean {
        val direction = getDirectionTo(tile)
        return isConnectedTo(tile, direction)
    }

    fun findAdjacentTilesInTransformedMaze(transformedMaze: Maze, tile: Tile): List<Tile> {
        val adjacentTiles = mutableListOf<Tile>()
        if (tile.i > transformedMaze.topBound) {
            val tileCandidate = transformedMaze.tiles[tile.i - 1][tile.j]
            adjacentTiles.add(tileCandidate)
        }
        if (tile.i < transformedMaze.bottomBound) {
            val tileCandidate = transformedMaze.tiles[tile.i + 1][tile.j]
            adjacentTiles.add(tileCandidate)

        }
        if (tile.j > transformedMaze.leftBound) {
            val tileCandidate = transformedMaze.tiles[tile.i][tile.j - 1]
            adjacentTiles.add(tileCandidate)
        }
        if (tile.j < transformedMaze.rightBound) {
            val tileCandidate = transformedMaze.tiles[tile.i][tile.j + 1]
            adjacentTiles.add(tileCandidate)
        }

        return adjacentTiles
    }

    fun Tile.markIfEnclosed(maze: Maze): Boolean {
        isProcessing = true
        if (isProcessed && !isEnclosed) {
            isProcessing = false
            return true
        }
        if (isProcessed && isEnclosed) {
            isProcessing = false
            return false
        }
        if (i == maze.topBound || i == maze.bottomBound || j == maze.leftBound || j == maze.rightBound) {
            isEnclosed = false
            isProcessing = false
            isProcessed = true
            return true
        }

        val adjacentTiles = findAdjacentTilesInTransformedMaze(maze, this)
        var result = false
        for (adjacentTile in adjacentTiles) {
            if (!adjacentTile.isProcessed && !adjacentTile.inCycle && !adjacentTile.isProcessing && !(!adjacentTile.isOriginal && adjacentTile.symbol == '|')) {
                result = result || adjacentTile.markIfEnclosed(maze)
            }
        }

        isEnclosed = !result
        isProcessing = false
        isProcessed = true
        return result
    }

    fun Tile.markAdjacentTilesReachable(maze: Maze) {
        val adjacentTiles = findAdjacentTilesInTransformedMaze(maze, this)
        for (adjacentTile in adjacentTiles) {
            if (!adjacentTile.inCycle && adjacentTile.isEnclosed) {
                adjacentTile.isEnclosed = false
                adjacentTile.markAdjacentTilesReachable(maze)
            }
        }
    }

    fun <T> List<List<T>>.transpose(): List<List<T>> {
        return (this[0].indices).map { i -> (this.indices).map { j -> this[j][i] } }
    }
    
    fun Maze.updateMazeIndices() {
        tiles.forEachIndexed { iIndex, tiles ->
            tiles.forEachIndexed { jIndex, tile ->
                tile.i = iIndex
                tile.j = jIndex
            }
        }
    }

    fun Maze.withVerticalConnectionTiles(connectedChar: Char, notConnectedChar: Char): Maze {
        val transformedMazeRaw = buildList {
            tiles.windowed(2).forEach {
                val (upperLine, lowerLine) = it
                val lineInBetween = upperLine.mapIndexed { index, tile ->
                    val newTile: Tile = if (tile.isConnectedTo(lowerLine[index])) {
                        Tile(connectedChar, -1, -1, inCycle = true, isOriginal = false)
                    } else {
                        Tile(notConnectedChar, -1, -1, inCycle = false, isOriginal = false)
                    }
                    return@mapIndexed newTile
                }
                add(upperLine)
                add(lineInBetween)
            }
            add(tiles.last())
        }
        val result = Maze(transformedMazeRaw)
        result.updateMazeIndices()
        return result
    }

    fun Maze.withHorizontalConnectionTiles(connectedChar: Char, notConnectedChar: Char): Maze {
        val transposedMaze = Maze(tiles.transpose()).withVerticalConnectionTiles(connectedChar, notConnectedChar)
        val resultMazeRaw = transposedMaze.tiles.transpose()
        val result = Maze(resultMazeRaw)
        result.updateMazeIndices()
        return result
    }

    fun Maze.withConnectionTiles(): Maze {
        return withVerticalConnectionTiles('!', '~').withHorizontalConnectionTiles('=', '~')
    }

    fun part2(input: List<String>): Int {
        val mazeRaw = input.mapIndexed { index, str ->
            str.mapIndexed { indexC, c ->
                Tile(c, index, indexC)
            }
        }
        val maze = Maze(mazeRaw)
        maze.markMainLoopTiles()
        val mazeWithConnections = maze.withConnectionTiles()

        val tileList = mazeWithConnections.tiles.flatten()
        for (tile in tileList) {
            if (!tile.inCycle && !tile.isProcessed) {
                tile.markIfEnclosed(mazeWithConnections)
            }
        }
        for (tile in tileList) {
            if (!tile.inCycle && !tile.isEnclosed) {
                tile.markAdjacentTilesReachable(mazeWithConnections)
            }
        }

        val result = tileList.count { it.isOriginal && !it.inCycle && it.isEnclosed }
        return result
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput).also { it.println() } == 8)

    val testInput2 = readInput("Day10_test_2")
    check(part2(testInput2).also { it.println() } == 4)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
