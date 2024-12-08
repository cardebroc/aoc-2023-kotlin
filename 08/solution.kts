import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

open class Position(
    val x: Int,
    val y: Int,
) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y)
    operator fun minus(other: Position) = Position(x - other.x, y - other.y)
    override operator fun equals(other: Any?) = other is Position && x == other.x && y == other.y
    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

interface IAmAntinode

interface IAmAntenna { val frequency: Char }

fun Position.toAntinode() = Antinode(x, y)

class Antenna(
    x: Int,
    y: Int,
    override val frequency: Char,
) : Position(x, y), IAmAntenna

class Antinode(
    x: Int,
    y: Int,
) : Position(x, y), IAmAntinode


class AntennaMap(positions: List<Position>) {
    val antennas = positions.filterIsInstance<Antenna>()
    val width = positions.maxOf { it.x }
    val height = positions.maxOf { it.y }
    val antinodes = mutableListOf<Antinode>()

    fun isInBounds(position: Position): Boolean = (0..width).contains(position.x) && (0..height).contains(position.y)

    fun markAntinodes() {
        antennas
            .groupBy { it.frequency }
            .forEach { (_, values) ->
                values.forEach { value1 ->
                    values.forEach { value2 ->
                        if (value1 != value2) {
                            val diff = value1 - value2

                            var d1 = (value1 + diff).toAntinode()
                            var d2 = (value2 - diff).toAntinode()
//                            with(antinodes) {  // part one
//                                add((value2 - diff).toAntinode())
//                                add((value1 + diff).toAntinode())
//                            }
                            while (isInBounds(d1)) {
                                antinodes.add(d1)
                                d1 = (d1 + diff).toAntinode()
                            }

                            while (isInBounds(d2)) {
                                antinodes.add(d2)
                                d2 = (d2 - diff).toAntinode()
                            }

                            antinodes.add(value1.toAntinode())
                            antinodes.add(value2.toAntinode())
                        }
                    }
                }
            }
    }
}

val positions: List<Position> = inputString
    .split("\n")
    .mapIndexed { j, line ->
        line.toList().mapIndexed { i, char ->
            if (char != '.') Antenna(i, j, char) else Position(i, j)
        }
    }.flatten()

AntennaMap(positions)
    .apply(AntennaMap::markAntinodes)
    .let { map ->
        map.antinodes.toSet().filter { (0..map.width).contains(it.x) && (0..map.height).contains(it.y) }
    }.map { "(${it.x},${it.y})" }.size
