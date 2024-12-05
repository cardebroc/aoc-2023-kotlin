import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

val (rulesInput, updatesInput) = inputString.split("\n\n")

class Rules(private val dependencies: Map<Int, List<Int>>) {
    fun validateUpdates(updates: List<Int>): Int? {
        updates.forEachIndexed { index, value ->
            val dependentOn = dependencies[value]
            dependentOn?.forEach {
                if (updates.contains(it) && updates.indexOf(it) > index) return null
            }
        }

        return updates[updates.size / 2]
    }

    fun fixBrokenUpdates(updates: List<Int>): Int? {
        if (validateUpdates(updates) != null) return null
        val temp = updates.toMutableList()
        val result = mutableListOf<Int>()

        while (temp.isNotEmpty()) {
            var nextValue = 0

            for (tvalue in temp) {
                if (dependencies.containsKey(tvalue)) {
                    val deps = dependencies[tvalue]!!
                    if (deps.any { temp.contains(it) }) continue
                }
                nextValue = tvalue
                result.add(nextValue)
                break
            }
            temp.remove(nextValue)
        }

        return result[result.size / 2]
    }
}

val individualRules = rulesInput
    .split("\n")
    .map { line ->
        val (first, second) = line.split("|")
        second.toInt() to first.toInt()
    }

val rules = individualRules
    .map { it.first }
    .toSet()
    .associateWith { key -> individualRules.filter { rule -> rule.first == key }.map { it.second }.toList() }
    .onEach(::println)
    .run(::Rules)

val updates = updatesInput
    .split("\n")
    .map { line -> line.split(",").map { it.toInt() } }


//updates.mapNotNull(rules::validateUpdates).onEach(::println).sum()
updates.mapNotNull(rules::fixBrokenUpdates).onEach(::println).sum()