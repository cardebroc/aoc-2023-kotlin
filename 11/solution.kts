import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("test.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }


class Stone(var number: Long) {
    fun blink(): Stone? {
        if (number == 0L) {
            number = 1L
        } else if (number.toString().length % 2 == 0) {
            val numberString = number.toString()
            val firstHalf = numberString.take(numberString.length / 2)
            val secondHalf = numberString.takeLast(numberString.length / 2)

            number = firstHalf.toLong()
            return Stone(secondHalf.toLong())
        } else {
            number *= 2024
        }

        return null
    }
}

fun countStones(stones: List<Stone>, depth: Int, memory: MutableMap<Pair<Int, Int>, Int> = mutableMapOf((0 to 1) to 0)): Long {
    return 0L
}

val stones = inputString.split(" ").map(String::toLong).map(::Stone).toMutableList()
//val stones = mutableListOf(Stone(0L))

repeat(75) { i->
    println("Run $i: ${stones.size} Stones")

    val newStones = mutableListOf<Pair<Int, Stone>>()
    stones.forEachIndexed { index, stone ->
        stone.blink()?.let { newStones.add(index to it) }
    }

    newStones.forEach { (index, stone) ->
        stones.add(index+1, stone)
    }
}

println(stones.size)
