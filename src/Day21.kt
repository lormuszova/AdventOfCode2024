import java.io.File
import kotlin.collections.set
import kotlin.math.abs

fun main() {
    Day21().partOne()
    Day21().partTwo()
}

class Day21 {
    val numericKeypad = mapOf(
        '1' to Pair(2, 0),
        '2' to Pair(2, 1),
        '3' to Pair(2, 2),
        '4' to Pair(1, 0),
        '5' to Pair(1, 1),
        '6' to Pair(1, 2),
        '7' to Pair(0, 0),
        '8' to Pair(0, 1),
        '9' to Pair(0, 2),
        '0' to Pair(3, 1),
        'A' to Pair(3, 2),
    )

    val directionalKeypad = mapOf(
        '^' to Pair(0, 1),
        'A' to Pair(0, 2),
        '<' to Pair(1, 0),
        'v' to Pair(1, 1),
        '>' to Pair(1, 2),
    )

    fun partOne() {
        val data = readData()

        var result = 0
        data.forEach { code ->
            val commandForFirstRobot = commandForNumericKeypadRobot(code)
            var commandForSecondRobot = commandForDirectionalKeypadRobot2(commandForFirstRobot)
            val commandForThirdRobot = commandForDirectionalKeypadRobot2(commandForSecondRobot)
            val numericValue = code.filter { it.isDigit() }.toInt()
            result += numericValue * commandForThirdRobot.length
        }

        println("Sum of complexities: $result")
    }

    fun partTwo() {

        buildMoveMap()

        val data = readData()
        var result = 0L

        data.forEach { code ->
            val commandForFirstRobot = commandForNumericKeypadRobot(code)
            var commandLists = commandForFirstRobot.split("A").filter { it.isNotEmpty() }
                .map { (it + "A").toList() }
            val oneMoveCommandList = commandLists.map { it.map { directionalKeypad[it]!! } }
            var previousPosition: Pair<Int, Int> = Pair(0, 2)
            var resultMap = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>()
            oneMoveCommandList.forEach { moveList ->
                moveList.forEach {
                    resultMap[previousPosition to it] =
                        resultMap.getOrDefault(previousPosition to it, 0) + 1
                    previousPosition = it
                }
            }
            repeat(25) {
                resultMap = commandForOneIteration(resultMap)
            }
            val numericValue = code.filter { it.isDigit() }.toInt()

            result += resultMap.values.sum() * numericValue.toLong()

        }
        println("Result: $result")
    }

    fun commandForNumericKeypadRobot(
        code: String, previousRobotPosition: Pair<Int, Int> = Pair(3, 2)
    ): String {
        var robotPosition: Pair<Int, Int> = previousRobotPosition
        var buttonPressedOnDirectionalKeypad = ""

        code.forEach { numericKey ->
            val (rowChange, colChange) = Pair(
                robotPosition.first - numericKeypad[numericKey]!!.first,
                robotPosition.second - numericKeypad[numericKey]!!.second
            )


            if (rowChange < 0) {

                if (colChange > 0) {
                    (1..abs(colChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad += '<'
                    }
                }

                if ((robotPosition == numericKeypad['1'] || robotPosition == numericKeypad['4'] || robotPosition == numericKeypad['7']) && (numericKey == '0' || numericKey == 'A')) {
                    if (colChange < 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad += '>'
                        }
                    }

                    (1..abs(rowChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad += 'v'
                    }

                } else {
                    (1..abs(rowChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad += 'v'
                    }


                    if (colChange < 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad += '>'
                        }
                    }
                }

            } else {
                if ((robotPosition == numericKeypad['0'] || robotPosition == numericKeypad['A']) && (numericKey == '1' || numericKey == '4' || numericKey == '7')) {
                    (1..abs(rowChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad += '^'
                    }

                    if (colChange > 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad += '<'
                        }
                    }
                    if (colChange < 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad += '>'
                        }
                    }
                } else {
                    if (colChange > 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad += '<'
                        }
                    }
                    (1..abs(rowChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad += '^'
                    }
                    if (colChange < 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad += '>'
                        }
                    }
                }


            }
            buttonPressedOnDirectionalKeypad += 'A'
            robotPosition = numericKeypad[numericKey]!!
        }
        return buttonPressedOnDirectionalKeypad
    }

    fun commandForDirectionalKeypadRobot2(
        code: String, previousPosition: Pair<Int, Int> = Pair(0, 2)
    ): String {
        var robotPosition: Pair<Int, Int> = previousPosition
        var buttonPressedOnDirectionalKeypad = StringBuilder()

        code.forEach { directionKey ->
            val (rowChange, colChange) = Pair(
                robotPosition.first - directionalKeypad[directionKey]!!.first,
                robotPosition.second - directionalKeypad[directionKey]!!.second
            )
            if (rowChange > 0) {
                if (colChange > 0) {
                    (1..abs(colChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad.append('<')
                    }
                }
                if (colChange < 0) {
                    (1..abs(colChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad.append('>')
                    }
                }
                (1..abs(rowChange)).forEach { i ->
                    buttonPressedOnDirectionalKeypad.append('^')
                }
            } else {
                if (directionKey != '<') {
                    if (colChange > 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad.append('<')
                        }
                    }

                    (1..abs(rowChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad.append('v')
                    }
                    if (colChange < 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad.append('>')
                        }
                    }
                } else {
                    if (colChange < 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad.append('>')
                        }
                    }
                    (1..abs(rowChange)).forEach { i ->
                        buttonPressedOnDirectionalKeypad.append('v')
                    }
                    if (colChange > 0) {
                        (1..abs(colChange)).forEach { i ->
                            buttonPressedOnDirectionalKeypad.append('<')
                        }
                    }
                }
            }
            buttonPressedOnDirectionalKeypad.append('A')
            robotPosition = directionalKeypad[directionKey]!!
        }

        return buttonPressedOnDirectionalKeypad.toString()
    }

    fun commandForOneIteration(previousResultMap: MutableMap<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>): MutableMap<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long> {
        val mapAfterIteration = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Long>()
        previousResultMap.forEach { previousMapEntry ->
            var position = Pair(0, 2)
            moveMap[previousMapEntry.key]!!.forEach { move ->
                mapAfterIteration[position to move] =
                    mapAfterIteration.getOrPut(position to move) { 0 } + previousMapEntry.value
                position = move
            }
        }
        return mapAfterIteration
    }

    val moveMap = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, MutableList<Pair<Int, Int>>>()
    fun buildMoveMap() {
        for (i in directionalKeypad.entries) {
            for (j in directionalKeypad.entries) {
                moveMap[i.value to j.value] = mutableListOf<Pair<Int, Int>>()

                val (rowChange, colChange) = Pair(
                    i.value.first - j.value.first, i.value.second - j.value.second
                )


                var listOfMoves = mutableListOf<Pair<Int, Int>>()


                if (rowChange > 0) {
                    if (i.key == '<') {
                        if (colChange > 0) {
                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['<']!!)
                            }
                        }

                        if (colChange < 0) {
                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['>']!!)
                            }
                        }

                        (1..abs(rowChange)).forEach { i ->
                            listOfMoves.add(directionalKeypad['^']!!)
                        }

                    } else {

                        if (colChange > 0) {

                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['<']!!)
                            }
                        }


                        (1..abs(rowChange)).forEach { i ->
                            listOfMoves.add(directionalKeypad['^']!!)
                        }

                        if (colChange < 0) {
                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['>']!!)
                            }
                        }
                    }
                } else {
                    if (j.key == '<') {
                        (1..abs(rowChange)).forEach { i ->
                            listOfMoves.add(directionalKeypad['v']!!)
                        }
                        if (colChange > 0) {
                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['<']!!)
                            }
                        }
                    } else {
                        if (colChange > 0) {
                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['<']!!)
                            }
                        }
                        (1..abs(rowChange)).forEach { i ->
                            listOfMoves.add(directionalKeypad['v']!!)
                        }
                        if (colChange < 0) {
                            (1..abs(colChange)).forEach { i ->
                                listOfMoves.add(directionalKeypad['>']!!)
                            }
                        }
                    }
                }
                listOfMoves.add(directionalKeypad['A']!!)
                moveMap[i.value to j.value] = listOfMoves
            }
        }
    }

    fun readData(): List<String> {
        val fileName = "src/resources/Day21"
        return File(fileName).readLines()
    }
}