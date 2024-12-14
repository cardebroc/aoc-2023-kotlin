import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

data class Plant(val x: Int, val y: Int, val value: Char) {
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    fun isAdjacent(other: Plant): Boolean {
        return ((x == other.x && (y-1..y+1).contains(other.y)) ||
                (y == other.y && (x-1..x+1).contains(other.x)))
                && value == other.value && this != other
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

fun MutableList<Plant>.extractAllAdjacentPlants(givenStart: Plant? = null): List<Plant> {
    val start = givenStart ?: this.removeFirst()

    val plot = mutableListOf(start)
    val adjacentPlants = filter { it.isAdjacent(start) }

//        println("Adjacent plants of plant (${start.x}, ${start.y}, ${start.value}): $adjacentPlants")

    adjacentPlants.forEach {
        remove(it)
        plot += extractAllAdjacentPlants(it)
    }

    return plot.toSet().toList()
}

fun Iterable<Plant>.plantGroups() = this.toMutableList().let {
        buildList {
            while (it.isNotEmpty()) { add(it.extractAllAdjacentPlants()) }
        }
    }


val plants = inputString
    .split("\n")
    .mapIndexed { y, line -> line.mapIndexed { x, char -> Plant(x, y, char) } }
    .flatten()

val plots = plants.plantGroups()


//region part one
plots.sumOf { plot ->
    val perimeter = plot.sumOf { plant -> 4 - plot.filter { it.isAdjacent(plant) }.size }
    perimeter * plot.size
}
//endregion

//region part two
plots.sumOf { plot ->
    val perimeter = plot.let { p ->
        val upperFence = p.filter { plant -> !p.any { it.x == plant.x && it.y == plant.y-1 } }.plantGroups().size
        val lowerFence = p.filter { plant -> !p.any { it.x == plant.x && it.y == plant.y+1 } }.plantGroups().size
        val leftFence = p.filter { plant -> !p.any { it.x == plant.x-1 && it.y == plant.y } }.plantGroups().size
        val rightFence = p.filter { plant -> !p.any { it.x == plant.x+1 && it.y == plant.y } }.plantGroups().size

        upperFence + lowerFence + rightFence + leftFence
    }
    perimeter * plot.size
}
//endregion
