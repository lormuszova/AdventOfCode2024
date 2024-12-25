package resources

import java.io.File

fun main() {
    Day25().part1()
}

class Day25 {
    fun part1() {
        val (keys, locks) = readData()
        println("Keys: ${keys.joinToString(", ") { it.joinToString("") }}")
        println("Locks: ${locks.joinToString(", ") { it.joinToString("") }}")

        var countOrPairs = 0
        locks.forEach { lock ->
            keys.forEach { key ->
                if (lock.zip(key).all { (l, k) -> l + k <= 7 }) {
                    countOrPairs++
                }
            }
        }
        println("Count of pairs: $countOrPairs")
    }

    private fun readData(): Pair<MutableList<IntArray>, MutableList<IntArray>> {
        val fileName = "src/resources/Day25"
        val keysAndLocks = File(fileName).readText().split("\n\n")
        val keys: MutableList<IntArray> = mutableListOf()
        val locks: MutableList<IntArray> = mutableListOf()
        keysAndLocks.forEach {
            val lines = it.lines()
            val columns = lines.first().length
            if (lines.first().count { c -> c == '#' } == 5) {
                val key = IntArray(columns) { 0 }
                lines.forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '#') key[index]++
                    }
                }
                keys.add(key)
            } else {
                val lock = IntArray(columns) { 0 }
                lines.forEach { line ->
                    line.forEachIndexed { index, c ->
                        if (c == '#') lock[index]++
                    }
                }
                locks.add(lock)
            }
        }
        return Pair(keys, locks)
    }
}