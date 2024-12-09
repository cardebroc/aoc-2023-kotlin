import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

//val expandedInput = inputString   // part one
//    .mapIndexed { index, char ->
//        mutableListOf<String>().apply {
//            for (i in 1..char.toString().toInt()) {
//                if (index % 2 == 0) add((index / 2).toString()) else add(".")
//            }
//        }
//    }
//    .flatten()
//    .filter { it != "" }
//    .toList()
//
//val replaceList = expandedInput.toMutableList()
//
//var i = 0
//while (i <= replaceList.lastIndex) {
//    if (replaceList[i] == ".") {
//        replaceList[i] = replaceList.removeLast()
//    }
//    while (replaceList.last() == ".") { replaceList.removeLast() }
//    i++
//}
//
//replaceList
//    .filter { it != "." }
//    .mapIndexed { index, value -> index to value }
//    .sumOf { (it.first * it.second.toInt()).toLong() }

//fun fitFileIntoSpace(space: List<String>, file: List<String>): List<String> = List<String>(space.size) {
//    for (byte in space) {
//        if (byte != ".") add(byte)
//    }
//    for (byte in file) add(byte)
//    for (diff in 0..<(space.size - this.size)) add(".")
//}

val (files, spaces) = inputString
    .mapIndexed { index, c -> index to c.toString() }
    .partition { (index, _) -> index % 2 == 0 }
    .let { (files, spaces) ->
        files.mapIndexed { index, value -> index to (1..value.second.toInt()).map { index.toString() } } to
                spaces.mapIndexed { index, value -> index to (1..value.second.toInt()).map { "." } }
    }

//println(spaces)

val orderedFiles = files.toMutableList()
val mutilatedSpaces = spaces.toMutableList()

for ((index, file) in files.reversed()) {
    println("Working $index")
    for (i in 0..<index) {
        val space = mutilatedSpaces[i]
        if (space.second.filter { it == "." }.size >= file.size) {
            val newSpace = buildList {
                for (byte in space.second) {
                    if (byte != ".") add(byte)
                }
                for (byte in file) add(byte)
                for (diff in 0..<(space.second.size - this.size)) add(".")
            }
            mutilatedSpaces[mutilatedSpaces.indexOf(space)] = space.first to newSpace
            orderedFiles[orderedFiles.indexOf(index to file)] = index to buildList { for (b in file) add(".") }
//            println("Placing $file in space no.${mutilatedSpaces[mutilatedSpaces.indexOf(space)]}")
            break
        }
    }
}

for (file in orderedFiles) {
    if (mutilatedSpaces.map { it.first }.contains(file.first)) {
        orderedFiles[orderedFiles.indexOf(file)] = file.first to file.second + mutilatedSpaces.first { it.first == file.first }.second
    }
}

for (space in mutilatedSpaces) {
    if (!orderedFiles.map { it.first }.contains(space.first)) {
        orderedFiles.add(space)
    }
}

//println(orderedFiles)

orderedFiles
    .asSequence()
    .sortedBy { it.first }
    .map { it.second }
    .flatten()
    .mapIndexed { index, s -> index to s }
    .sumOf { (index, s) ->
        if (s == ".") 0L else (index * s.toInt()).toLong()
    }

//println(orderedFiles.map { it.second }.zip(mutilatedSpaces.map { it.second }))
