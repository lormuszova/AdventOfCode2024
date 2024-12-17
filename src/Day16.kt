import utils.bidirectionalAStar
import java.io.File

fun main() {
    Day16().partOne()
}
class Day16 {
    fun partOne() {
        val data = readData()
        var start: Pair<Int, Int> = Pair(0, 0)
        var end: Pair<Int, Int> = Pair(0, 0)
        data.withIndex().flatMap { (rowIndex, row) ->
            row.withIndex().map { (colIndex, cell) ->
                when (cell) {
                    'S' -> start = Pair(rowIndex, colIndex)
                    'E' -> end = Pair(rowIndex, colIndex)
                }

            }
        }
        val path = bidirectionalAStar(data, start, end)
        println("Path: $path") //87480, 86480 too high 87480
    }

    fun partTwo() {
        val data = readData()
        var start: Pair<Int, Int> = Pair(0, 0)
        var end: Pair<Int, Int> = Pair(0, 0)
        data.withIndex().flatMap { (rowIndex, row) ->
            row.withIndex().map { (colIndex, cell) ->
                when (cell) {
                    'S' -> start = Pair(rowIndex, colIndex)
                    'E' -> end = Pair(rowIndex, colIndex)
                }

            }
        }
        val path = bidirectionalAStar(data, start, end)
        println("Path: $path") //87480, 86480 too high 87480
    }

    private fun readData() : Array<Array<Char>>{
        val fileName = "src/resources/Day16"
        return File(fileName).readLines().map {it.toCharArray().toTypedArray()}.toTypedArray()

    }
}