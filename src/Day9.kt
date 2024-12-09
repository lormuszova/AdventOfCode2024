import java.io.File

fun main() {
    partOne()
    partTwo()
}

private fun partOne() {
    val data = readData()
    var result = 0L
    data.forEach { line->
        var idCounter = 0
        val newList = mutableListOf<Int?>()
        for(i in line.indices) {
            if(i.mod(2) == 0) {
                for(j in 1.. line[i].digitToInt()) {
                    newList.add(idCounter)
                }
                idCounter++
            } else {
                for(j in 1..line[i].digitToInt()) {
                    newList.add(null)
                }
            }
        }
        var i = 0
        while(newList.contains(null)) {
                if (newList[i] == null) {
                    newList[i] = newList.last()
                    newList.removeLast()
                }
                while (newList.isNotEmpty() && newList.last() == null) {
                    newList.removeAt(newList.size - 1)
                }
            if (newList[i] == null) {
                newList[i] = newList.last()
                newList.removeLast()
            }
            i++
        }
        newList.forEachIndexed { index, number ->
             result += number?.times(index) ?: 0
        }
        println("Result: $result")
    }
}

private fun partTwo() {
    val data = readData()

    val files = mutableListOf<Pair<Int, Int>>()
    var idCounter = 0
    var fileLocation = 0
    var result = 0L

    data.forEach { line ->
        val newList = mutableListOf<Int?>()
        for (i in line.indices) {
            if (i.mod(2) == 0) {
                files.add(Pair(idCounter, line[i].digitToInt()))
                for (j in 1..line[i].digitToInt()) {
                    newList.add(idCounter)
                    fileLocation++
                }
                idCounter++
            } else {
                for (j in 1..line[i].digitToInt()) {
                    newList.add(null)
                }
            }
        }


        val highestNumber = newList.last()
        highest@ for (i in highestNumber!! downTo 0) {
            var counter = 1
            val iIndices = newList.indexOf(i)
            if(iIndices == -1) {
                continue
            }
            while (newList[iIndices] == newList[iIndices + counter]) {
                counter++
                if (iIndices + counter == newList.size) {
                    break
                }
            }

            var temp = 0
            for (j in 1..<iIndices) {
                if (newList[j] == null) {
                    temp++
                } else {
                    temp = 0
                }
                if (temp == counter) {
                    for (k in 0..<counter) {
                        newList[j - k] = i
                        newList[iIndices + k] = null
                    }
                    continue@highest
                }

            }
        }
        newList.forEachIndexed { index, i ->
            result += i?.times(index) ?: 0
        }
        println("Result: $result")
    }


}

private fun readData(): List<String> {
    val fileName = "src/resources/Day9"
    return File(fileName).readLines()
}