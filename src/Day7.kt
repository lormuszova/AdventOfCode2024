import java.io.File

fun main() {
    partOne()
    partTwo()
}

private fun partOne() {
    val data = readData()
    var totalSum = 0L
    data.forEach outer@{ (key, values) ->
        totalSum += verifyAllCombinations(values, key)
    }
    println("Total sum: $totalSum")
}

private fun partTwo() {
    val data = readData()
    var totalSum = 0L
    data.forEach outer@{ (key, values) ->
        totalSum += verifyAllCombinationsWithConcatenation(values, key)

    }
    println("Total sum: $totalSum")
}

private fun verifyAllCombinations(values: List<Long>, key: Long) : Long {

    val operators = Array(values.size - 1) { "+" }
    for(i in 1..<values.size) {
        if (isDivisible(values[i], key)) {
            operators[i - 1] = "*"
        }
    }
    createAllPossibleCombinationOfPlusAndMultiply(operators).forEach { combination ->
        val result = createAndCountEquation(values, combination)
        if (result == key) {
            return key
        }
    }
    return 0
}

private fun verifyAllCombinationsWithConcatenation(values: List<Long>, key: Long) : Long {

    val operators = Array(values.size - 1) { "+" }
    createAllPossibleCombination(operators).forEach { combination ->
        val result = createAndCountEquationWithListOfResultsWithConcatenation(values, combination)
        if (result.first == key) {
            return key
        }
    }
    return 0
}

private fun readData(): List<Pair<Long, List<Long>>> {
    val fileName = "src/resources/Day7"
    val file = File(fileName).readLines()
    return file.map { line ->
        val parts = line.split(": ")
        val key = parts[0].toLong()
        val values = parts[1].split(" ").map { it.toLong() }
        key to values
    }
}

private fun createAllPossibleCombinationOfPlusAndMultiply(operators: Array<String>): List<Array<String>> {
    val combinations = mutableListOf<Array<String>>()
    generateCombinations(operators, 0, combinations)
    return combinations
}

private fun createAllPossibleCombination(operators: Array<String>): List<Array<String>> {
    val combinations = mutableListOf<Array<String>>()
    generateCombinationsWithConcatenation(operators, 0, combinations)
    return combinations
}


private fun generateCombinations(operators: Array<String>, index: Int, combinations: MutableList<Array<String>>) {
    if (index == operators.size) {
        combinations.add(operators.copyOf())
        return
    }
    operators[index] = "+"
    generateCombinations(operators, index + 1, combinations)
    operators[index] = "*"
    generateCombinations(operators, index + 1, combinations)
}

private fun generateCombinationsWithConcatenation(operators: Array<String>, index: Int, combinations: MutableList<Array<String>>) {
    if (index == operators.size) {
        combinations.add(operators.copyOf())
        return
    }
    val ops = arrayOf("+", "*", "|")
    for (op in ops) {
        operators[index] = op
        generateCombinationsWithConcatenation(operators, index + 1, combinations)
    }
}

private fun createAndCountEquationWithListOfResultsWithConcatenation(values: List<Long>, operators: Array<String>): Pair<Long, List<Long>> {
    var result = values[0]
    val listOfResults = mutableListOf<Long>()
    operators.forEachIndexed { index, operator ->
        if (operator == "+") {
            result += values[index + 1]
        } else if(operator == "*") {
            result *= values[index + 1]
        } else {
            val concatenated = (result.toString() + values[index + 1].toString()).toLong()
            result = concatenated
        }
    }
    return Pair(result, listOfResults)
}

private fun createAndCountEquation(values: List<Long>, operators: Array<String>): Long {
    var result = values[0]
    operators.forEachIndexed { index, operator ->
        if (operator == "+") {
            result += values[index + 1]
        } else {
            result *= values[index + 1]
        }
    }
    return result
}

private fun isDivisible(a: Long, result: Long): Boolean {
    return result % a == 0L
}