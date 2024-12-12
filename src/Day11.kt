import kotlinx.coroutines.*
import java.io.File
import java.math.BigInteger

fun main() {
    partOne()
    partTwo()
}

private fun partOne() {
    var data = readData()
    for (i in 1..25) {
        val newNumbers = mutableListOf<Long>()
        data.forEach { number ->
            newNumbers.addAll(applyRules(number))
        }
        data = newNumbers
    }
    println("Result: ${data.size}")
}

private fun partTwo() {
    val data = readData()
    var stoneCounter =  BigInteger.ZERO

    data.forEachIndexed { index, number ->
        stoneCounter += applyRules(number.toBigInteger(), 75)
    }
    println("Result: $stoneCounter")
}

private val memo = mutableMapOf<Pair<BigInteger, Int>, BigInteger>()

private fun applyRules(number: BigInteger, repetitions: Int): BigInteger {
    val key = Pair(number, repetitions)
    if (memo.containsKey(key)) {
        return memo[key]!!
    }

     val result =  when {
        number == BigInteger.ZERO -> {
            if(repetitions ==0) BigInteger.ONE else {
                applyRules(BigInteger.ONE, repetitions - 1)
            }
        }
        number.toString().length % 2 == 0 -> {
            if(repetitions ==0) BigInteger.ONE else {
                val numberString = number.toString()
                val numberLength = numberString.length
                val firstHalf = numberString.subSequence(0, numberLength / 2).toString().toBigInteger()
                val secondHalf = numberString.subSequence(numberLength / 2, numberLength).toString().toBigInteger()
                val firstResult = applyRules(firstHalf, repetitions - 1)
                val secondResult = applyRules(secondHalf, repetitions - 1)

                firstResult + secondResult
            }
        }
        else -> {
            if(repetitions ==0) BigInteger.ONE else {
                applyRules(number * BigInteger("2024"), repetitions - 1)
            }
        }
    }
    memo[key] = result
    return result
}

private fun applyRules(number: Long): List<Long> {
    return when {
        number == 0L -> listOf(1L)
        number.toString().length % 2 == 0 -> listOf(
            number.toString().subSequence(0, number.toString().length / 2).toString().toLong(),
            number.toString().subSequence(number.toString().length / 2, number.toString().length).toString().toLong()
        )
        else -> listOf(number * 2024L)
    }
}

private fun readData():  MutableList<Long> {
    val file = File("src/resources/Day11").readText()

    return file.split(" ").map{ it.toLong()}.toMutableList()
}