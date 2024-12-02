import java.io.File
import kotlin.math.abs

fun main() {
    firstPart()
}

fun firstPart() {
    val data: List<MutableList<Int>> = readData().map { line -> line.split(" ").map { it.toInt() }.toMutableList() }
    var safeRoads = 0
    data.forEach { line ->
        val size = line.size

        if (isRoadSafe(line)) {
            safeRoads++
            println("Safe roads: $line")

        } else {
            for (i in 0..<size) {
                if (isRoadSafe(line.filterIndexed { index, _ -> index != i })) {
                    safeRoads++
                    println("Safe roads: $line")

                    break
                }
            }
        }


    }
    println("Safe roads: $safeRoads")
}

private fun isRoadSafe(road: List<Int>): Boolean {
    val order: Boolean = isAscending(road[0], road[1])
    for(i in 0..< road.size - 1) {
        if (!isDifferenceSafe(road[i], road[i + 1])) {
            return false
        }
        if(isAscending(road[i], road[i + 1]) != order) {
            return false
        }
    }
    return true
}

private fun readData(): List<String> {
    val fileName = "src/resources/Day2"
    val file = File(fileName).readLines()
    return file

}

private fun isAscending(firstNumber: Int, secondNumber: Int): Boolean {
    return secondNumber - firstNumber > 0
}

private fun isDifferenceSafe(firstNumber: Int, secondNumber: Int): Boolean {
    return abs(secondNumber - firstNumber) in 1..3
}