import java.io.File
import java.io.BufferedReader
import java.lang.Thread.sleep
import kotlin.concurrent.thread

val bufferedReader: BufferedReader = File("test.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

enum class Direction(val pos: Position) {
    UP(Position(0, -1)) {
        override fun next() = RIGHT
    },
    DOWN(Position(0, +1)) {
        override fun next() = LEFT
    },
    LEFT(Position(-1, 0)) {
        override fun next() = UP
    },
    RIGHT(Position(+1, 0)) {
        override fun next() = DOWN
    };

    abstract fun next(): Direction
}

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
}

class Guard(
    startingPosition: Position,
    private val obstacles: List<Position>,
    private val size: Int,
    wholeMap: Map<Position, Char>,
    private val verbose: Boolean = false
) {
    private var currentPosition = startingPosition
    private val visited = mutableSetOf(startingPosition)
    private var direction: Direction = Direction.UP
    private val updatedMap = wholeMap.toMutableMap()

    private fun move() {
        while (obstacles.contains(currentPosition + direction.pos)) {
            direction = direction.next()
        }
        currentPosition += direction.pos
        visited.add(currentPosition)
        updatedMap[currentPosition] = 'X'
        val img = updatedMap
            .let { predictedMap ->
                mutableListOf<String>().apply {
                    predictedMap.keys.map { it.y }.toSet().forEach { y ->
                        add(predictedMap.filter { it.key.y == y }.values.joinToString(""))
                    }
                }
            }.joinToString("\n")
        if (verbose) {
//            val printThread = thread {
//                println()
//                sleep(10)
//            }

            clearTerminal()
            println(img)
//            printThread.join()
        }
    }

    fun moveSmart() {
        val nextPOI = when (direction) {
            Direction.UP -> updatedMap.filter { it.key.x == currentPosition.x && it.key.y < currentPosition.y }.takeIf { it.isNotEmpty() }?.minBy { it.key.y }?.key
            Direction.DOWN -> updatedMap.filter { it.key.x == currentPosition.x && it.key.y > currentPosition.y }.takeIf { it.isNotEmpty() }?.minBy { it.key.y }?.key
            Direction.LEFT -> updatedMap.filter { it.key.x < currentPosition.x && it.key.y == currentPosition.y }.takeIf { it.isNotEmpty() }?.minBy { it.key.x }?.key
            Direction.RIGHT -> updatedMap.filter { it.key.x > currentPosition.x && it.key.y == currentPosition.y }.takeIf { it.isNotEmpty() }?.minBy { it.key.x }?.key
        } ?: getEdge()

        visited.apply {
            val vertical = listOf(nextPOI.y, currentPosition.y)
            val horizontal = listOf(nextPOI.x, currentPosition.x)
            when (direction) {
                Direction.UP,
                Direction.DOWN -> {
                    (vertical.min()..vertical.max()).forEach { y -> add(Position(currentPosition.x, y))}
                }

                Direction.LEFT,
                Direction.RIGHT -> {
                    (horizontal.min()..horizontal.max()).forEach { x -> add(Position(x, currentPosition.y))}
                }
            }
        }
        direction = direction.next()
    }

    private fun getEdge(): Position {
        return when (direction) {
            Direction.UP -> Position(currentPosition.x, -1)
            Direction.DOWN -> Position(currentPosition.x, size+1)
            Direction.LEFT -> Position(-1, currentPosition.y)
            Direction.RIGHT -> Position(size+1, currentPosition.y)
        }
    }

    fun patrol(startingPosition: Position? = null, startingDirection: Direction? = null): Set<Position> {
        startingPosition?.let { currentPosition = it }
        startingDirection?.let { direction = it }

        while ((0..<size).contains(currentPosition.x) && (0..<size).contains(currentPosition.y)) {
            moveSmart()
        }
        return visited.toSet()
    }

}

fun clearTerminal() {
    ProcessBuilder("clear")
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor()
//    Runtime.getRuntime().exec("clear")
}

val map = inputString
    .split("\n")
    .mapIndexed { y, line ->
        line
            .toList()
            .mapIndexed { x, char ->
                Position(x, y) to char
            }
    }
    .flatten()
    .associate { it.first to it.second }

println("Created Map")

val obstacles = map.filter { it.value == '#' }.map { it.key }
println("Found Obstacles")

val startingPosition = map.filter { it.value == '^' }.map { it.key }.single()

val guard = Guard(startingPosition, obstacles, inputString.split("\n").size, map, false)

guard.patrol().size
//guard.patrol().also { visited ->
//    val predictedMap = map.map { (pos, char) -> if (visited.contains(pos)) pos to 'X' else pos to char }
//        .associate { it.first to it.second }
//        .let { predictedMap ->
//            mutableListOf<String>().apply {
//                predictedMap.keys.map { it.y }.toSet().forEach { y ->
//                    add(predictedMap.filter { it.key.y == y }.values.joinToString(""))
//                }
//            }
//        }.joinToString("\n")
//
//    println(predictedMap)
//}.size - 1
