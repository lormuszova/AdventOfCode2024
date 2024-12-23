import java.io.File
import kotlin.collections.mutableListOf

fun main() {
    Day22().part1()
    Day22().part2()
}

class Day22 {
    fun part1() {
        val data = readData()
        var result = 0L
        data.forEach {
            var i = 0
            var localResult = it
            while(i < 2000){
                val first = firstSequence(localResult)
                val second = secondSequence(first)
                localResult = thirdSequence(second)
                i++
            }
            result += localResult
        }
        println("Part 1: $result")
    }

    fun part2() {
        val data = readData()
        var totalMapSequence = mutableMapOf<String, Long>()
        data.forEach {
            var i = 0
            val resultFirstPlace = mutableListOf<Long>()
            var localResult = it
            val mapSequence = mutableMapOf<String, Long>()
            while(i < 2000){
                val first = firstSequence(localResult)
                val second = secondSequence(first)
                localResult = thirdSequence(second)
                i++
                resultFirstPlace.add(localResult % 10)
            }
            for(i in 0..resultFirstPlace.size - 5){
                val key = "${resultFirstPlace[i + 1] - resultFirstPlace[i]}${resultFirstPlace[i + 2]-resultFirstPlace[i + 1]}${resultFirstPlace[i + 3]-resultFirstPlace[i + 2]}${resultFirstPlace[i + 4]-resultFirstPlace[i + 3]}"
                mapSequence.putIfAbsent(key, resultFirstPlace[i + 4])
            }
            if (totalMapSequence.isEmpty()) {
                totalMapSequence = mapSequence
            } else {
                mapSequence.forEach { (key, value) ->
                    totalMapSequence[key] = value + totalMapSequence.getOrDefault(key, 0)
                }
            }
        }
        val maxEntry = totalMapSequence.maxByOrNull { it.value }
        println("Part 2: ${maxEntry?.value}")
    }

    val mask = 0xFFFFFFL
    private fun firstSequence(number: Long): Long {
        return (number shl 6).xor(number).and(mask)
    }

    private fun secondSequence(number: Long): Long {
        return (number shr 5).xor(number).and(mask)
    }

    private fun thirdSequence(number: Long): Long {
        return (number shl 11).xor(number).and(mask)
    }

    private fun readData(): List<Long> {
        val fileName = "src/resources/Day22"
        return File(fileName).readLines().map { it.toLong() }
    }
}

//879776047 too low