import java.io.File
import java.io.BufferedReader
import kotlin.math.abs

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

val (firstList, secondList) = inputString.split("\n").let { lines ->
    val firstL = mutableListOf<Int>()
    val secondL = mutableListOf<Int>()

    lines.forEach { line ->
        firstL.add(line.split("   ")[0].toInt())
        secondL.add(line.split("   ")[1].toInt())
    }

    firstL to secondL
}

//region part one
firstList.sortedBy { it }
    .zip(secondList.sortedBy { it })
    .sumOf { (firstListElement, secondListElement) ->
        abs(firstListElement - secondListElement)
    }
//endregion
//region part two
firstList.sumOf { id ->
    val factor = secondList.filter { it == id }.size
    id * factor
}
//endregion
