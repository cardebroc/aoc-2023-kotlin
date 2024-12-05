import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

val xmasRegex = """(?=XMAS)|(?=SAMX)""".toRegex()

val rows = inputString.split("\n")
val cols = mutableListOf<String>().apply {
    for (i in 0..<rows[0].length) {
        add(rows.map { it[i] }.joinToString(""))
    }
}.toList()

val rowsMatrix = rows.map { row -> row.toList() }

val diagonalsTLBR = mutableListOf<String>().apply {
    //region direction top-left->bottom-right
    for (i in rowsMatrix[0].lastIndex downTo 0) {
        var line = ""
        for (j in rowsMatrix.indices) {
            if (i+j > rowsMatrix[0].lastIndex) break
            line += rowsMatrix[j][i+j]
        }
        add(line)
    }

    for (j in 1..rowsMatrix.lastIndex) {
        var line = ""
        for (i in rowsMatrix[0].indices) {
            if (i+j > rowsMatrix.lastIndex) break
            line += rowsMatrix[j+i][i]
        }
        add(line)
    }
    //endregion
}.toList()

val diagonalsBLTR = mutableListOf<String>().apply {
    //region bottom-left->top-right
    for (j in rowsMatrix.indices) {
        var line = ""
        for (i in rowsMatrix[0].indices) {
            if (j-i < 0) break
            line += rowsMatrix[j-i][i]
        }
        add(line)
    }

    for (i in 1..rowsMatrix[0].lastIndex) {
        var line = ""
        for (j in rowsMatrix.lastIndex downTo 0) {
            if (i+(rowsMatrix.lastIndex-j) > rowsMatrix[0].lastIndex) break
            line += rowsMatrix[j][i+(rowsMatrix.lastIndex-j)]
        }
        add(line)
    }
    //endregion
}.toList()

val forwardsAndBackwards = rows.sumOf { row -> xmasRegex.findAll(row).toList().size }
val upAndDown = cols.sumOf { col -> xmasRegex.findAll(col).toList().size }
val allDiagonals = (diagonalsTLBR + diagonalsBLTR).sumOf { diagonal -> xmasRegex.findAll(diagonal).toList().size }

println(forwardsAndBackwards)
println(upAndDown)
println(allDiagonals)

println(forwardsAndBackwards + upAndDown + allDiagonals)

//region part two
var counter = 0


for (j in 1..<rowsMatrix.lastIndex) {
    for (i in 1..<rowsMatrix[j].lastIndex) {
        if (rowsMatrix[i][j] == 'A') {
            val ms = setOf('S', 'M')
            val diagonal1 = setOf(rowsMatrix[i-1][j-1], rowsMatrix[i+1][j+1])
            val diagonal2 = setOf(rowsMatrix[i-1][j+1], rowsMatrix[i+1][j-1])

            if (diagonal1 == ms && diagonal2 == ms) {
                counter++
            }
        }
    }
}
//

println(counter)
