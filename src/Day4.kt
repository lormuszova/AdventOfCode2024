import java.io.File

fun main() {
    firstPart()
    partTwo()
}

private fun readData(): List<String> {
    val fileName = "src/resources/Day4"
    val file = File(fileName).readLines()
    return file
}

private fun firstPart() {
    val data = readData()
    var counter = 0
    data.forEachIndexed { index, s ->
        for (i in s.indices) {
            if (s[i] == 'X') {
                if (i + 3 < s.length) {
                    if (s[i + 1] == 'M' && s[i + 2] == 'A' && s[i + 3] == 'S') {
                        counter++
                    }
                }
                if (i - 3 >= 0) {
                    if (s[i - 1] == 'M' && s[i - 2] == 'A' && s[i - 3] == 'S') {
                        counter++

                    }
                }

                if (index + 3 < data.size) {
                    if (data[index + 1][i] == 'M' && data[index + 2][i] == 'A' && data[index + 3][i] == 'S') {
                        counter++

                    }
                }
                if (index - 3 >= 0) {
                    if (data[index - 1][i] == 'M' && data[index - 2][i] == 'A' && data[index - 3][i] == 'S') {
                        counter++

                    }
                }

                if (index + 3 < data.size && i + 3 < s.length) {
                    if (data[index + 1][i + 1] == 'M' && data[index + 2][i + 2] == 'A' && data[index + 3][i + 3] == 'S') {
                        counter++

                    }
                }
                if (index - 3 >= 0 && i + 3 < s.length) {
                    if (data[index - 1][i + 1] == 'M' && data[index - 2][i + 2] == 'A' && data[index - 3][i + 3] == 'S') {
                        counter++

                    }
                }

                if (index + 3 < data.size && i - 3 >= 0) {
                    if (data[index + 1][i - 1] == 'M' && data[index + 2][i - 2] == 'A' && data[index + 3][i - 3] == 'S') {
                        counter++
                    }
                }
                if (index - 3 >= 0 && i - 3 >= 0) {
                    if (data[index - 1][i - 1] == 'M' && data[index - 2][i - 2] == 'A' && data[index - 3][i - 3] == 'S') {
                        counter++
                    }
                }
            }
        }
    }
    println("Counter: $counter")
}

private fun partTwo() {
    val data = readData()
    var counter = 0
    data.forEachIndexed { index, s ->
        for (i in s.indices) {
            if (s[i] == 'A') {
                if (index - 1 >= 0 && i - 1 >= 0 && i + 1 < s.length && index + 1 < data.size) {
                    if (data[index - 1][i - 1] == 'M' && data[index - 1][i + 1] == 'M' && data[index + 1][i - 1] == 'S' && data[index + 1][i + 1] == 'S') {
                        counter++
                    }
                    if (data[index - 1][i - 1] == 'M' && data[index + 1][i - 1] == 'M' && data[index + 1][i + 1] == 'S' && data[index - 1][i + 1] == 'S') {
                        counter++
                    }
                    if (data[index + 1][i + 1] == 'M' && data[index + 1][i - 1] == 'M' && data[index - 1][i - 1] == 'S' && data[index - 1][i + 1] == 'S') {
                        counter++
                    }
                    if (data[index - 1][i + 1] == 'M' && data[index + 1][i + 1] == 'M' && data[index - 1][i - 1] == 'S' && data[index + 1][i - 1] == 'S') {
                        counter++
                    }
                }
            }
        }
    }
    println("Counter: $counter")
}