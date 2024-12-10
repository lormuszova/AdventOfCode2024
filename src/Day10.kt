import utils.Direction
import java.io.File

fun main() {
    val data = readData()
    var result = 0
    var resultPart2 = 0
    for (i in data.indices) {
        for (j in data[i].indices) {
            if (data[i][j] == '0') {
                var startingPoints = mutableListOf(Pair(i, j))
                for (k in 0..8) {
                    val nextStepList = mutableListOf<Pair<Int, Int>>()
                    startingPoints.forEach {
                        Direction.entries.forEach { direction ->
                            if (nextStepPossible(it, k, direction, data) != Pair(-1, -1)) {
                                nextStepList.add(nextStepPossible(it, k, direction, data))
                            }
                        }
                    }
                    startingPoints = nextStepList
                }
                result += startingPoints.toSet().size
                resultPart2 += startingPoints.size
            }
        }
    }
    println("Result part 1: $result")
    println("Result part 2: $resultPart2")
}

private fun nextStepPossible(
    actualPosition: Pair<Int, Int>,
    stepCounter: Int,
    direction: Direction,
    data: List<String>
): Pair<Int, Int> {
    when (direction) {
        Direction.DOWN -> {
            if (actualPosition.first + 1 < data.size) {
                if (data[actualPosition.first + 1][actualPosition.second].digitToInt() == stepCounter + 1) {
                    return Pair(actualPosition.first + 1, actualPosition.second)
                }
            }
        }
        Direction.UP -> {
            if (actualPosition.first - 1 >= 0) {
                if (data[actualPosition.first - 1][actualPosition.second].digitToInt() == stepCounter + 1) {
                    return Pair(actualPosition.first - 1, actualPosition.second)
                }
            }
        }
        Direction.LEFT -> {
            if (actualPosition.second - 1 >= 0) {
                if (data[actualPosition.first][actualPosition.second - 1].digitToInt() == stepCounter + 1) {
                    return Pair(actualPosition.first, actualPosition.second - 1)
                }
            }
        }
        Direction.RIGHT -> {
            if (actualPosition.second + 1 < data[0].length) {
                if (data[actualPosition.first][actualPosition.second + 1].digitToInt() == stepCounter + 1) {
                    return Pair(actualPosition.first, actualPosition.second + 1)
                }
            }
        }
    }
    return Pair(-1, -1)
}

private fun readData(): List<String> {
    val fileName = "src/resources/Day10"
    return File(fileName).readLines()
}