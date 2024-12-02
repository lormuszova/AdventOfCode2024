import utils.quickSort
import java.io.File
import kotlin.math.abs

fun main() {
    //firstPart(readData().first, readData().second)
    secondPart(readData().first, readData().second)
}

private fun readData(): Pair<Array<Int>, Array<Int>> {
    val fileName = "src/resources/Day1"
    val file = File(fileName).readLines()
    val firstArray : Array<Int> = Array<Int>(file.size) {0}
    val secondArray: Array<Int> = Array<Int>(file.size) {0}
    file.forEachIndexed { index, s ->
        val numbers = s.split("   ")
        firstArray[index] = numbers[0].toInt()
        secondArray[index] = numbers[1].toInt()
    }
    return Pair(firstArray, secondArray)
}

fun secondPart(firstArray: Array<Int>, secondArray: Array<Int>) {
    var totalDistance = 0
    quickSort(firstArray).let { firstArrayOrdered ->
        quickSort(secondArray).toList().let { secondArrayOrdered ->
            var secondArrayIndex = 0
            var appearances = 0
            firstArrayOrdered.forEachIndexed { index, i ->
                //println("index: $index - ${firstArrayOrdered[index]} ${secondArrayOrdered[index]}, secondaryindex: $secondArrayIndex ${secondArrayOrdered[secondArrayIndex]}")

                if(index > 1) {
                    if (firstArrayOrdered[index] == firstArrayOrdered[index - 1]) {
                        totalDistance += appearances * firstArrayOrdered[index]
                        return@forEachIndexed
                    } else {
                        appearances = 0
                    }
                }

                secondArrayOrdered.indexOf(firstArrayOrdered[index]).let {
                    if(it != -1) {
                        secondArrayIndex = it
                    }
                }

                while (firstArrayOrdered[index] == secondArrayOrdered[secondArrayIndex]) {
                    appearances++
                    secondArrayIndex++
                    totalDistance += firstArrayOrdered[index]
                    println("index: $index - ${firstArrayOrdered[index]} ${secondArrayOrdered[index]}, secondaryindex: $secondArrayIndex ${secondArrayOrdered[secondArrayIndex]}")
                }
            }
        }
    }

    println("Total distance: $totalDistance")
}



fun firstPart(firstArray: Array<Int>, secondArray: Array<Int>) {
    var distance = 0
    quickSort(firstArray).let { firstArrayOrdered ->
        quickSort(secondArray).let { secondArrayOrdered ->
            for(i in firstArrayOrdered.indices) {
                distance += abs(firstArrayOrdered[i] - secondArrayOrdered[i])
            }
        }
    }
    println("Total distance: $distance")
}