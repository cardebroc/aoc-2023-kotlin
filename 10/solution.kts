import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("test.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

data class Point(val x: Int, val y: Int, val value: Int)

class TopologicalMap(private val points: List<Point>) {
    private val trailheads = points.filter { it.value == 0 }

    private fun calculateScore(point: Point): Set<Point> {
        if (point.value == 9) return setOf(point)

        val surroundingPoints = listOfNotNull(
            points.firstOrNull { it.x == point.x+1 && it.y == point.y },
            points.firstOrNull { it.x == point.x-1 && it.y == point.y },
            points.firstOrNull { it.x == point.x && it.y == point.y+1 },
            points.firstOrNull { it.x == point.x && it.y == point.y-1 },
        )


        if (surroundingPoints.all { it.value <= point.value }) return setOf()

        return surroundingPoints
            .filter { it.value == point.value+1 }
            .map { value -> calculateScore(value) }
            .takeIf { it.isNotEmpty() }
            ?.reduce { acc, value -> acc + value }
            ?.toSet() ?: setOf()
    }

    private fun calculateRating(point: Point): Int {
        if (point.value == 9) return 1

        val surroundingPoints = listOfNotNull(
            points.firstOrNull { it.x == point.x + 1 && it.y == point.y },
            points.firstOrNull { it.x == point.x - 1 && it.y == point.y },
            points.firstOrNull { it.x == point.x && it.y == point.y + 1 },
            points.firstOrNull { it.x == point.x && it.y == point.y - 1 },
        )

        if (surroundingPoints.all { it.value <= point.value }) return 0

        return surroundingPoints
            .filter { it.value == point.value + 1 }
            .sumOf { calculateRating(it) }
    }

    fun topologyScore(): Int = trailheads.sumOf { calculateScore(it).size }
    fun topologyRating(): Int = trailheads.sumOf { calculateRating(it) }
}

val tm = inputString
    .split("\n")
    .mapIndexed { j, line -> line.mapIndexed { i, char -> Point(i, j, char.toString().toInt()) } }
    .flatten()
    .run(::TopologicalMap)

//println(tm.trailheads.map { tm.calculateScore(it) })
println(tm.topologyScore())
println(tm.topologyRating())
