import java.io.File

fun main() {
    //firstPart()
    //secondPart()
    secondPartBruteForce()
}

private fun readData(): List<CharArray> {
    val fileName = "src/resources/Day6"
    val file = File(fileName).readLines().map { it.toCharArray() }
    return file
}

private fun firstPart() {
    val data = readData()
    var direction: Direction
    var actualPosition: Pair<Int, Int>
    findInitialPosition(data).let {
        actualPosition = it.first
        direction = it.second
    }

    while(true) {
        val possibleNextStep = nextStep(direction, actualPosition)
        if (isNextStepWithinTheBounds(possibleNextStep, data)) {
            if (isObstaclePresent(possibleNextStep, data)) {
                direction = changeDirection(direction)
                //println("Changing direction to $direction")
            } else {
                data[actualPosition.second][actualPosition.first] = 'X'
                actualPosition = possibleNextStep
                println("Moving to $possibleNextStep")
            }
        } else {
            data[actualPosition.second][actualPosition.first] = 'X'
            println("Next step $possibleNextStep is out of bounds")
            break
        }
    }

    val steps = data.sumOf { x -> x.count { it == 'X' } }
    println("Steps: $steps")
}

private fun secondPartBruteForce() {
    var data = readData()
    var direction: Direction
    var actualPosition: Pair<Int, Int>


    var breakCounter = 0


    data.forEachIndexed { index, chars ->
        chars.forEachIndexed { index2, _ ->
            var leadOutside = false
            var isLoop = false
            data = readData()
            data[index][index2] = '#'
            findInitialPosition(data).let {
                actualPosition = it.first
                direction = it.second
            }
            println("Initial position: $actualPosition, direction: $direction, addedBreak: $index, $index2")

            while(!leadOutside && !isLoop) {
                val possibleNextStep = nextStep(direction, actualPosition)
                if (isNextStepWithinTheBounds(possibleNextStep, data)) {
                    if (isObstaclePresent(possibleNextStep, data)) {
                        direction = changeDirection(direction)
                        //println("Changing direction to $direction")
                    } else {
                        if(data[actualPosition.second][actualPosition.first] == direction.name[0]) {
                            breakCounter++
                            isLoop = true
                        } else {
                            data[actualPosition.second][actualPosition.first] = direction.name[0]
                        }
                        actualPosition = possibleNextStep
                        //println("Moving to $possibleNextStep")
                    }
                } else {
                    data[actualPosition.second][actualPosition.first] = 'X'
                    println("Next step $possibleNextStep is out of bounds")
                    leadOutside = true
                }
            }
        }
    }

    println("Break counter: $breakCounter")
}

private fun secondPart() {
    val data = readData()
    var direction: Direction
    var actualPosition: Pair<Int, Int>
    findInitialPosition(data).let {
        actualPosition = it.first
        direction = it.second
    }

    println("Initial position: $actualPosition, direction: $direction")
    val obstructions: MutableSet<Pair<Int, Int>> = mutableSetOf()

    while(true) {
        var possibleNextStep = nextStep(direction, actualPosition)
        if (isNextStepWithinTheBounds(possibleNextStep, data)) {
            if (isObstaclePresent(possibleNextStep, data)) {
                //obstructions += checkObstructionOnRightSide(actualPosition, direction, data)
                direction = changeDirection(direction)
                //println("Changing direction to $direction")
            } else {
                obstructions += checkObstructionOnRightSide(actualPosition, direction, data)
//                val directionSymbol = when(direction) {
//                    Direction.RIGHT -> '_'
//                    Direction.UP -> '|'
//                    Direction.LEFT -> '_'
//                    Direction.DOWN -> '|'
//                }
                data[actualPosition.second][actualPosition.first] = 'X'
                actualPosition = possibleNextStep
                //println("Moving to $possibleNextStep")
            }
        } else {
            //data[actualPosition.second][actualPosition.first] = 'X'
           // obstructions += checkObstructionOnRightSide(actualPosition, direction, data)
            //println("Next step $possibleNextStep is out of bounds")
            break
        }
    }


    println("Obstructions: ${obstructions.size}") //2052 too high asi, 2051 high, 1703 - too low, 1708 - wrong
    println("Obstructions: $obstructions")
}

private fun checkIfObstructionCreateLoop(startPosition: Pair<Int, Int>, initialDirection: Direction): Boolean {
    val data = readData()
    var direction = initialDirection
    var actualPosition = startPosition
    var possibleNextStep: Pair<Int, Int> = Pair(-1, -1)
    var counter = 0
    println("Infinite loop for position: $actualPosition, direction: $direction")
    while(possibleNextStep != startPosition) {
        possibleNextStep = nextStep(direction, actualPosition)
        counter++
        if (isNextStepWithinTheBounds(possibleNextStep, data)) {
            if (isObstaclePresent(possibleNextStep, data)) {
                direction = changeDirection(direction)
                //println("Changing direction to $direction")
            } else {
                if(data[actualPosition.second][actualPosition.first] == '.') {
                    data[actualPosition.second][actualPosition.first] = 'X'
                } else if(data[actualPosition.second][actualPosition.first] == 'X') {
                    return true
                }
                //println("Data: ${data[actualPosition.second][actualPosition.first]}")
                data[actualPosition.second][actualPosition.first] = 'X'
                actualPosition = possibleNextStep
                //println("Moving to $possibleNextStep")
            }
        } else {
            //data[actualPosition.second][actualPosition.first] = 'X'
            println("Next step $possibleNextStep is out of bounds")
            return false
        }
    }
    return true
}

private fun checkObstructionOnRightSide(position: Pair<Int, Int>, direction: Direction, data: List<CharArray>): Set<Pair<Int, Int>> {
    val changedDirection = changeDirection(direction)
    //println("Original direction: $direction, changed direction: $changedDirection")
    var obstructions: MutableSet<Pair<Int, Int>> = mutableSetOf()
    when (changedDirection) {
        Direction.RIGHT -> {
            for(i in position.first + 1..<data[0].size) {
                //println("Checking Right side, position: ${position.first}, i: $i")

                if (isObstaclePresent(Pair(i, position.second), data)) {

                    if(data[position.second][i-1] == 'X') {
                        //data[position.second - 1][position.first]  = '#'
                        if(checkIfObstructionCreateLoop(Pair(position.first, position.second), changedDirection)) {
                            obstructions.add(Pair(position.first, position.second - 1))
                        }
                        //print("Obstacle found at ${Pair(position.first, position.second - 1)}")
                    }

                }
            }
        }
        Direction.UP -> {
            for(i in position.second - 1 downTo 0) {
                if (isObstaclePresent(Pair(position.first, i), data)) {
                    if(data[i+1][position.first] == 'X') {
                        //data[position.second][position.first - 1] = '#'
                        if(checkIfObstructionCreateLoop(Pair(position.first, position.second), changedDirection)) {
                            obstructions.add(Pair(position.first - 1, position.second))
                        }
                        //print("Obstacle found at ${Pair(position.first -1, position.second)}")
                    }
                }
            }
        }
        Direction.LEFT -> {
            for(i in position.first -1  downTo 0) {
                if (isObstaclePresent(Pair(i, position.second), data)) {
                    if(data[position.second][i+1] == 'X') {
                        //data[position.second + 1][position.first] = '#'
                        if(checkIfObstructionCreateLoop(Pair(position.first, position.second), changedDirection)) {
                            obstructions.add(Pair(position.first, position.second + 1))
                        }
                        //print("Obstacle found at ${Pair(position.first, position.second + 1)}")
                    }
                }
            }
        }
        Direction.DOWN -> {
            for (i in position.second + 1 ..<data.size) {
                if (isObstaclePresent(Pair(position.first, i), data)) {
                    if(data[i-1][position.first] == 'X') {
                        //data[position.second][position.first + 1] = '#'
                        if(checkIfObstructionCreateLoop(Pair(position.first, position.second), changedDirection)) {

                            obstructions.add(Pair(position.first + 1, position.second))
                        }
                       //print("Obstacle found at ${Pair(position.first + 1, position.second)}")
                    }
                }
            }
        }
    }
    return obstructions
}

private fun findInitialPosition(data: List<CharArray>): Pair<Pair<Int, Int>, Direction> {
    var direction: Direction = Direction.RIGHT
    var actualPosition = Pair(0, 0)
    data.forEachIndexed { indexLine, line ->
        line.forEachIndexed { indexColumn, char ->
            when (char) {
                '>' -> {
                    direction = Direction.RIGHT
                    actualPosition = Pair(indexColumn, indexLine)
                }
                '^' -> {
                    direction = Direction.UP
                    actualPosition = Pair(indexColumn, indexLine)
                }
                '<' -> {
                    direction = Direction.LEFT
                    actualPosition = Pair(indexColumn, indexLine)
                }
                'v' -> {
                    direction = Direction.DOWN
                    actualPosition = Pair(indexColumn, indexLine)
                }
                else -> {}
            }
        }
    }
    return Pair(actualPosition, direction)
}

private fun isObstaclePresent(position: Pair<Int, Int>, data: List<CharArray>): Boolean {
    return data[position.second][position.first] == '#'
}

private fun isNextStepWithinTheBounds(position: Pair<Int, Int>, data: List<CharArray>): Boolean {
    return (position.first >= 0 && position.first < data[0].size && position.second >= 0 && position.second < data.size)
}

private fun changeDirection(direction: Direction): Direction {
    return when (direction) {
        Direction.RIGHT -> Direction.DOWN
        Direction.UP -> Direction.RIGHT
        Direction.LEFT -> Direction.UP
        Direction.DOWN -> Direction.LEFT
    }
}

private fun nextStep(direction: Direction, position: Pair<Int, Int>): Pair<Int, Int> {
    return when (direction) {
        Direction.RIGHT -> Pair(position.first + 1, position.second)
        Direction.UP -> Pair(position.first, position.second - 1)
        Direction.LEFT -> Pair(position.first - 1, position.second)
        Direction.DOWN -> Pair(position.first, position.second + 1)
    }
}

private enum class Direction {
    RIGHT, UP, LEFT, DOWN
}

