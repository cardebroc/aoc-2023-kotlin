import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

val (firstList, secondList) = inputString.split("\n").let { lines ->
    val firstL = mutableListOf<Pair<String, Int>>()
    val secondL = mutableListOf<Pair<String, Int>>()

    lines.forEachIndexed { index, line ->
        firstL.add(line.split("   ")[0] to index)
        secondL.add(line.split("   ")[1] to index)
    }

    firstL to secondL
}

println(firstList)
println(secondList)
