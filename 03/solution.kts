import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

val mulRegex = """mul\((\d+),(\d+)\)""".toRegex()

mulRegex.findAll(inputString)
    .toList()
    .sumOf { matchResult ->
        matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
    }

val mulRegexAdvanced = """(?:mul\((\d+),(\d+)\))|(?:do\(\))|(?:don't\(\))""".toRegex()

var isActive = true
mulRegexAdvanced.findAll(inputString)
    .toList()
    .sumOf { matchResult ->
        if (listOf("do()", "don't()").contains(matchResult.value)) {
            isActive = matchResult.value == "do()"
        } else {
            if (isActive) return@sumOf matchResult.groupValues[1].toInt() * matchResult.groupValues[2].toInt()
        }
        0
    }
