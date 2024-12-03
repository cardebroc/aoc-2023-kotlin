import java.io.File
import java.io.BufferedReader
import kotlin.math.abs

val bufferedReader: BufferedReader = File("test.txt").bufferedReader()
val inputString = bufferedReader.use { it.readText() }

class Sequence(val numbers: List<Int>) {
    private val ascending = numbers.first() - numbers.last() < 0

    fun validate(): Boolean {
        println("Validating $numbers")

        val iter = numbers.iterator()
        var previous = iter.next()

        while (iter.hasNext()) {
            val current = iter.next()
            if (isInvalid(previous, current)) return false

            previous = current
        }
        println("SAFE")
        return true
    }

    private fun isInvalid(previous: Int, current: Int): Boolean {
        val invalidStep = !(1..3).contains(abs(previous - current))
        val notMonotonous = ascending && previous >= current || !ascending && previous <= current
        return invalidStep || notMonotonous
    }

}

fun windows(numbers: List<Int>): List<List<Int>> = numbers.mapIndexed { index, _ ->
    numbers.slice((0..numbers.lastIndex) - setOf(index))
}

val sequences = inputString.split("\n")
    .map { line -> line.split(" ").map { it.toInt() } }
    .map(::Sequence)

sequences.filter { seq ->
    seq.validate() || windows(seq.numbers).map(::Sequence).any(Sequence::validate)
}.size
