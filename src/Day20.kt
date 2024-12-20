import utils.Direction
import java.io.File
import kotlin.math.abs
import kotlin.math.sign
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        Day20().partOne()
        Day20().partTwo()
    }
    println("Execution time: $time")
}

class Day20 {
    fun partOne() {
        val mapOfRacetrack = readData()
        var start: Pair<Int, Int> = Pair(0, 0)
        var end: Pair<Int, Int> = Pair(0, 0)
        mapOfRacetrack.withIndex().flatMap { (rowIndex, row) ->
            row.withIndex().map { (colIndex, cell) ->
                when (cell) {
                    'S' -> start = Pair(rowIndex, colIndex)
                    'E' -> end = Pair(rowIndex, colIndex)
                }

            }
        }
        var actualPosition = start
        var steps = 0
        val path: LinkedHashMap<Pair<Int, Int>, Int> = linkedMapOf(start to 0)
        while (actualPosition != end) {
            steps++
            val nextPosition = findNext(mapOfRacetrack, actualPosition, path)
            path.put(actualPosition, steps)
            actualPosition = nextPosition
        }
        path.put(end, steps + 1)
        var canSavePs = 0

        path.forEach {
            val (coordinates, steps) = it
            val (row, col) = coordinates

            Direction.entries.forEach { jumpDirection ->
                val stepsAtJump =
                    path.get(row + 2 * jumpDirection.coordinates.first to col + 2 * jumpDirection.coordinates.second)
                if (stepsAtJump != null && stepsAtJump >= steps + 2 + 100) {
                    canSavePs++
                }
            }
        }
        println("Can save: $canSavePs")
    }

    fun partTwo() {
        val mapOfRacetrack = readData()
        var start: Pair<Int, Int> = Pair(0, 0)
        var end: Pair<Int, Int> = Pair(0, 0)
        mapOfRacetrack.withIndex().flatMap { (rowIndex, row) ->
            row.withIndex().map { (colIndex, cell) ->
                when (cell) {
                    'S' -> start = Pair(rowIndex, colIndex)
                    'E' -> end = Pair(rowIndex, colIndex)
                }

            }
        }
        var actualPosition = start
        var steps = 0
        val path: LinkedHashMap<Pair<Int, Int>, Int> = linkedMapOf(start to 0)
        while (actualPosition != end) {
            steps++
            val nextPosition = findNext(mapOfRacetrack, actualPosition, path)
            path.put(actualPosition, steps)
            actualPosition = nextPosition
        }

        path.put(end, steps + 1)
        var canSavePs = 0
        val possibleJumps = mutableListOf<Pair<Int, Int>>()
        for (i in 0..20) {
            for (j in 0..20) {
                if (i + j <= 20) {
                    possibleJumps.add(Pair(i, j))
                    if (i != 0) {
                        possibleJumps.add(Pair(-i, j))
                    }
                    if (j != 0) {
                        possibleJumps.add(Pair(i, -j))
                    }
                    if (i != 0 && j != 0) {
                        possibleJumps.add(Pair(-i, -j))
                    }
                }
            }
        }

        path.forEach {
            val (coordinates, steps) = it
            val (row, col) = coordinates
            val jumps: MutableMap<Int, MutableList<Pair<Int, Int>>> = mutableMapOf()

            for ((x, y) in possibleJumps) {
                var stepsAtJump = path.get(row + x to col + y)
                val stepsWithShortcut = steps + abs(x) + abs(y)
                if (stepsAtJump != null && stepsAtJump >= stepsWithShortcut + 100) {

                    if (jumps.containsKey(stepsAtJump - stepsWithShortcut)) {
                        jumps[stepsAtJump - stepsWithShortcut]?.add(Pair(x.sign, y.sign))
                    } else {
                        jumps.put(
                            stepsAtJump - stepsWithShortcut,
                            mutableListOf(Pair(x.sign, y.sign))
                        )
                    }
                    canSavePs++
                }
            }
        }
        println("Can save: $canSavePs")
    }

    private fun findNext(
        mapOfRacetrack: Array<Array<Char>>,
        actualPosition: Pair<Int, Int>,
        path: LinkedHashMap<Pair<Int, Int>, Int>
    ): Pair<Int, Int> {
        Direction.entries.forEach { direction ->
            val (row, col) = direction.coordinates
            if ((mapOfRacetrack[actualPosition.first + row][actualPosition.second + col] == '.' || mapOfRacetrack[actualPosition.first + row][actualPosition.second + col] == 'E') && !path.contains(
                    Pair(actualPosition.first + row, actualPosition.second + col)
                )
            ) {
                return Pair(actualPosition.first + row, actualPosition.second + col)
            }
        }
        return throw Exception("No direction found")
    }

    private fun readData(): Array<Array<Char>> {
        val fileName = "src/resources/Day20"
        return File(fileName).readLines().map { it.toCharArray().toTypedArray() }.toTypedArray()

    }
}