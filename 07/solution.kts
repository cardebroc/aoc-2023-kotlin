import java.io.File
import java.io.BufferedReader

val bufferedReader: BufferedReader = File("input.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

enum class Operators(val value: String) {
    MUL("*"),
    ADD("+"),
    CONCAT("||");

    companion object {
        fun populate(size: Int, withConcat: Boolean = false): List<List<Operators>> = mutableListOf<List<Operators>>().apply {
            if (size == 1) {
                add(listOf(ADD))
                add(listOf(MUL))
                if (withConcat) { add(listOf(CONCAT)) }
                return@apply
            }

            for (l in populate(size - 1, withConcat)) {
                add(listOf(ADD) + l)
                add(listOf(MUL) + l)
                if (withConcat) { add(listOf(CONCAT) + l) }
            }
        }.toList()
    }
}

class Configuration(line: String) {
    val result: Long = line.split(": ")[0].toLong()
    val parts: List<Long> = line.split(": ")[1].split(" ").map { it.toLong() }

    fun validate(): Boolean = Operators.populate(parts.lastIndex, withConcat = true).any { ops ->
        parts.reduceIndexed { index, acc, value ->
            when(ops[index-1]) {
                Operators.MUL -> acc * value
                Operators.ADD -> acc + value
                Operators.CONCAT -> { "$acc$value".toLong() }
            }
        }.let { res ->
            println("Is $res == $result?\nEquation: $parts\nOperators: $ops\n")
            return@any res == result
        }
    }
}

inputString.split("\n").map(::Configuration).filter(Configuration::validate).sumOf { it.result }
