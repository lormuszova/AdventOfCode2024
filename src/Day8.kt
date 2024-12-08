import java.io.File
import kotlin.math.abs

fun main() {
    partOne()
    partTwo()
}

private fun partOne() {
    val data = readData()

    val uniqueChars = data.flatMap { it.toList() }.filter { it != '.' }.toSet()
    val coordinatesOfElements: MutableMap<Char, MutableList<Pair<Int, Int>>> = mutableMapOf()
    uniqueChars.forEach {
        data.forEachIndexed { index, line ->
            line.forEachIndexed { index2, char ->
                if (char == it) {
                    coordinatesOfElements.computeIfAbsent(char) { mutableListOf() }
                        .add(Pair(index, index2))
                }
            }
        }
    }
    coordinatesOfElements.forEach {
        it.value.forEachIndexed { index, pair ->
            val x = pair.first
            val y = pair.second
            for (i in index + 1..<it.value.size) {
                val x2 = it.value[i].first
                val y2 = it.value[i].second

                val firstNewNodeX = x + (x - x2)
                val firstNewNodeY = y + (y - y2)

                val secondNewNodeX = x2 + (x2 - x)
                val secondNewNodeY = y2 + (y2 - y)

                if (!elementOutOfBounds(firstNewNodeX, firstNewNodeY, data)) {
                    data[firstNewNodeX][firstNewNodeY] = '#'
                }

                if (!elementOutOfBounds(secondNewNodeX, secondNewNodeY, data)) {
                    data[secondNewNodeX][secondNewNodeY] = '#'
                }
            }
        }
    }
    println("Nodes: ${data.forEach { row -> println(row.joinToString(" ")) }}")
    println("New nodes: ${data.sumOf { x -> x.count { it == '#' } }}")
}

private fun partTwo() {
    val data = readData()
    val uniqueChars = data.flatMap { it.toList() }.filter { it != '.' }.toSet()
    val coordinatesOfElements: MutableMap<Char, MutableList<Pair<Int, Int>>> = mutableMapOf()
    uniqueChars.forEach {
        data.forEachIndexed { index, line ->
            line.forEachIndexed { index2, char ->
                if (char == it) {
                    coordinatesOfElements.computeIfAbsent(char) { mutableListOf() }
                        .add(Pair(index, index2))
                }
            }
        }
    }
    coordinatesOfElements.forEach {
        it.value.forEachIndexed { index, pair ->
            val x = pair.first
            val y = pair.second
            for (i in index + 1..<it.value.size) {
                val x2 = it.value[i].first
                val y2 = it.value[i].second

                var newNodeX = x
                var newNodeY = y

                val gcd = greatestCommonDivisor(abs(x - x2), abs(y - y2))
                val stepX = if (gcd != 0) (x - x2) / gcd else x - x2
                val stepY = if (gcd != 0) (y - y2) / gcd else y - y2


                while (!elementOutOfBounds(newNodeX, newNodeY, data)) {
                    data[newNodeX][newNodeY] = '#'
                    newNodeX += stepX
                    newNodeY += stepY
                }

                newNodeX = x
                newNodeY = y

                while (!elementOutOfBounds(newNodeX, newNodeY, data)) {
                    data[newNodeX][newNodeY] = '#'
                    newNodeX -= stepX
                    newNodeY -= stepY
                }
            }
        }
    }
    println("Nodes: ${data.forEach { row -> println(row.joinToString(" ")) }}")
    println("New nodes: ${data.sumOf { x -> x.count { it == '#' } }}")
}


private fun greatestCommonDivisor(a: Int, b: Int): Int {
    if (b == 0) return a
    return greatestCommonDivisor(b, a % b)
}

private fun elementOutOfBounds(x: Int, y: Int, data: Array<Array<Char>>): Boolean {
    return x < 0 || y < 0 || x >= data.size || y >= data[0].size
}

private fun readData(): Array<Array<Char>> {
    val fileName = "src/resources/Day8"
    return File(fileName).readLines().map { it.toCharArray().toTypedArray() }.toTypedArray()
}