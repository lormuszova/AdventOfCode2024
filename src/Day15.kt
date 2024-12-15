import utils.Direction
import java.io.File

fun main() {
    Day15().partOne()
    Day15().partTwo()
}

class Day15 {
    fun partOne() {
        val (map, movements) = readData()
        var robotPosition = map.withIndex().flatMap { (rowIndex, row) ->
            row.withIndex().mapNotNull { (colIndex, char) ->
                if (char == '@') Pair(rowIndex, colIndex) else null
            }
        }.first()
        val directions: List<Direction> = movements.map {
            when (it) {
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                else -> throw IllegalArgumentException("Invalid movement character: $it")
            }
        }
        directions.forEach { direction ->
            when (direction) {
                Direction.RIGHT -> {
                    var (wall, box) = wallOrBoxInTheWay(map, robotPosition, direction)
                    if (wall) return@forEach
                    var boxCounter = 0
                    while (box) {
                        boxCounter++
                        val result = wallOrBoxInTheWay(
                            map,
                            Pair(robotPosition.first, robotPosition.second + boxCounter),
                            direction
                        )
                        if (result.first) return@forEach
                        box = result.second

                    }
                    map[robotPosition.first][robotPosition.second] = '.'
                    map[robotPosition.first][robotPosition.second + 1] = '@'
                    robotPosition = Pair(robotPosition.first, robotPosition.second + 1)


                    for (i in 1..boxCounter) {
                        map[robotPosition.first][robotPosition.second + i] = 'O'
                    }
                }

                Direction.LEFT -> {
                    var (wall, box) = wallOrBoxInTheWay(map, robotPosition, direction)
                    if (wall) return@forEach
                    var boxCounter = 0
                    while (box) {
                        boxCounter++
                        val result = wallOrBoxInTheWay(
                            map,
                            Pair(robotPosition.first, robotPosition.second - boxCounter),
                            direction
                        )
                        if (result.first) return@forEach
                        box = result.second

                    }
                    map[robotPosition.first][robotPosition.second] = '.'
                    map[robotPosition.first][robotPosition.second - 1] = '@'
                    robotPosition = Pair(robotPosition.first, robotPosition.second - 1)

                    for (i in 1..boxCounter) {
                        map[robotPosition.first][robotPosition.second - i] = 'O'
                    }
                }

                Direction.UP -> {
                    var (wall, box) = wallOrBoxInTheWay(map, robotPosition, direction)
                    if (wall) return@forEach
                    var boxCounter = 0
                    while (box) {
                        boxCounter++
                        val result = wallOrBoxInTheWay(
                            map,
                            Pair(robotPosition.first - boxCounter, robotPosition.second),
                            direction
                        )
                        if (result.first) return@forEach
                        box = result.second

                    }
                    map[robotPosition.first][robotPosition.second] = '.'
                    map[robotPosition.first - 1][robotPosition.second] = '@'
                    robotPosition = Pair(robotPosition.first - 1, robotPosition.second)

                    for (i in 1..boxCounter) {
                        map[robotPosition.first - i][robotPosition.second] = 'O'
                    }
                }

                Direction.DOWN -> {
                    var (wall, box) = wallOrBoxInTheWay(map, robotPosition, direction)
                    if (wall) return@forEach
                    var boxCounter = 0
                    while (box) {
                        boxCounter++
                        val result = wallOrBoxInTheWay(
                            map,
                            Pair(robotPosition.first + boxCounter, robotPosition.second),
                            direction
                        )
                        if (result.first) return@forEach
                        box = result.second

                    }
                    map[robotPosition.first][robotPosition.second] = '.'
                    map[robotPosition.first + 1][robotPosition.second] = '@'
                    robotPosition = Pair(robotPosition.first + 1, robotPosition.second)

                    for (i in 1..boxCounter) {
                        map[robotPosition.first + i][robotPosition.second] = 'O'
                    }
                }
            }
        }
        val result = map.withIndex().sumOf { (rowIndex, row) ->
            row.withIndex().sumOf { (colIndex, char) ->
                if (char == 'O') rowIndex * 100 + colIndex else 0
            }
        }

        println("Sum of coordinate: $result")
    }

    fun partTwo() {
        val (map, movements) = readData()

        val directions: List<Direction> = movements.map {
            when (it) {
                '>' -> Direction.RIGHT
                '<' -> Direction.LEFT
                '^' -> Direction.UP
                'v' -> Direction.DOWN
                else -> throw IllegalArgumentException("Invalid movement character: $it")
            }
        }
        val wideMap = map.map { row ->
            row.flatMap { char ->
                when (char) {
                    '#' -> listOf('#', '#')
                    '@' -> listOf('@', '.')
                    'O' -> listOf('[', ']')
                    '.' -> listOf('.', '.')
                    else -> listOf(char, char)
                }
            }.toCharArray()
        }

        var robotPosition = wideMap.withIndex().flatMap { (rowIndex, row) ->
            row.withIndex().mapNotNull { (colIndex, char) ->
                if (char == '@') Pair(rowIndex, colIndex) else null
            }
        }.first()

        directions.forEach { direction ->
            when (direction) {
                Direction.RIGHT -> {
                    var (wall, openBox, closeBox) = wallOrBigBoxInTheWay(
                        wideMap,
                        robotPosition,
                        direction
                    )
                    if (wall) return@forEach
                    var boxCounter = 0
                    while (openBox || closeBox) {
                        boxCounter += 2
                        val result = wallOrBigBoxInTheWay(
                            wideMap,
                            Pair(robotPosition.first, robotPosition.second + boxCounter),
                            direction
                        )
                        if (result.first) return@forEach
                        openBox = result.second
                        closeBox = result.third

                    }
                    wideMap[robotPosition.first][robotPosition.second] = '.'
                    wideMap[robotPosition.first][robotPosition.second + 1] = '@'
                    robotPosition = Pair(robotPosition.first, robotPosition.second + 1)


                    for (i in 1..boxCounter) {
                        if (i % 2 == 0) wideMap[robotPosition.first][robotPosition.second + i] = ']'
                        else wideMap[robotPosition.first][robotPosition.second + i] = '['
                    }
                }

                Direction.LEFT -> {
                    var (wall, openBox, closeBox) = wallOrBigBoxInTheWay(
                        wideMap,
                        robotPosition,
                        direction
                    )
                    if (wall) return@forEach
                    var boxCounter = 0
                    while (openBox || closeBox) {
                        boxCounter += 2
                        val result = wallOrBigBoxInTheWay(
                            wideMap,
                            Pair(robotPosition.first, robotPosition.second - boxCounter),
                            direction
                        )
                        if (result.first) return@forEach
                        openBox = result.second
                        closeBox = result.third

                    }
                    wideMap[robotPosition.first][robotPosition.second] = '.'
                    wideMap[robotPosition.first][robotPosition.second - 1] = '@'
                    robotPosition = Pair(robotPosition.first, robotPosition.second - 1)


                    for (i in 1..boxCounter) {
                        if (i % 2 != 0) wideMap[robotPosition.first][robotPosition.second - i] = ']'
                        else wideMap[robotPosition.first][robotPosition.second - i] = '['
                    }
                }

                Direction.UP -> {
                    val (wall, openBox, closeBox) = wallOrBigBoxInTheWay(
                        wideMap,
                        robotPosition,
                        direction
                    )
                    if (wall) return@forEach
                    if (openBox || closeBox) {
                        val boxesToMove = mutableListOf<Pair<Int, Int>>()
                        boxesToMove.addAll(moveBoxesUpOrDown(wideMap, robotPosition, Direction.UP))

                        if (boxesToMove.contains(Pair(-1, -1))) return@forEach
                        val originalValues = boxesToMove.map { wideMap[it.first][it.second] }
                        boxesToMove.forEach { pair ->
                            wideMap[pair.first][pair.second] = '.'
                        }
                        boxesToMove.forEachIndexed { index, pair ->
                            wideMap[pair.first - 1][pair.second] = originalValues[index]
                        }
                    }
                    wideMap[robotPosition.first][robotPosition.second] = '.'
                    wideMap[robotPosition.first - 1][robotPosition.second] = '@'
                    robotPosition = Pair(robotPosition.first - 1, robotPosition.second)
                }

                Direction.DOWN -> {
                    val (wall, openBox, closeBox) = wallOrBigBoxInTheWay(
                        wideMap,
                        robotPosition,
                        direction
                    )
                    if (wall) return@forEach
                    if (openBox || closeBox) {
                        val boxesToMove = mutableListOf<Pair<Int, Int>>()
                        boxesToMove.addAll(
                            moveBoxesUpOrDown(
                                wideMap,
                                robotPosition,
                                Direction.DOWN
                            )
                        )

                        if (boxesToMove.contains(Pair(-1, -1))) return@forEach
                        val originalValues = boxesToMove.map { wideMap[it.first][it.second] }
                        boxesToMove.forEach { pair ->
                            wideMap[pair.first][pair.second] = '.'
                        }
                        boxesToMove.forEachIndexed { index, pair ->
                            wideMap[pair.first + 1][pair.second] = originalValues[index]
                        }
                    }
                    wideMap[robotPosition.first][robotPosition.second] = '.'
                    wideMap[robotPosition.first + 1][robotPosition.second] = '@'
                    robotPosition = Pair(robotPosition.first + 1, robotPosition.second)
                }
            }
        }

        val result = wideMap.withIndex().sumOf { (rowIndex, row) ->
            row.withIndex().sumOf { (colIndex, char) ->
                if (char == '[') rowIndex * 100 + colIndex else 0
            }
        }

        println("Sum of coordinate: $result")
    }

    private fun moveBoxesUpOrDown(
        map: List<CharArray>,
        position: Pair<Int, Int>,
        direction: Direction
    ): MutableList<Pair<Int, Int>> {
        val (wall, openBox, closeBox) = wallOrBigBoxInTheWay(map, position, direction)
        val boxesToMove = mutableListOf<Pair<Int, Int>>()
        val dir = if (direction == Direction.UP) -1 else 1

        if (wall) {
            boxesToMove.add(Pair(-1, -1))
            return boxesToMove
        }

        if (openBox) {
            boxesToMove.add(Pair(position.first + dir, position.second))
            boxesToMove.add(Pair(position.first + dir, position.second + 1))
            boxesToMove.addAll(
                moveBoxesUpOrDown(
                    map,
                    Pair(position.first + dir, position.second),
                    direction
                )
            )
            boxesToMove.addAll(
                moveBoxesUpOrDown(
                    map,
                    Pair(position.first + dir, position.second + 1),
                    direction
                )
            )
        }

        if (closeBox) {
            boxesToMove.add(Pair(position.first + dir, position.second))
            boxesToMove.add(Pair(position.first + dir, position.second - 1))
            boxesToMove.addAll(
                moveBoxesUpOrDown(
                    map,
                    Pair(position.first + dir, position.second),
                    direction
                )
            )
            boxesToMove.addAll(
                moveBoxesUpOrDown(
                    map,
                    Pair(position.first + dir, position.second - 1),
                    direction
                )
            )
        }

        return boxesToMove
    }


    private fun wallOrBoxInTheWay(
        map: List<CharArray>,
        position: Pair<Int, Int>,
        direction: Direction
    ): Pair<Boolean, Boolean> {
        return when (direction) {
            Direction.RIGHT -> {
                Pair(
                    map[position.first][position.second + 1] == '#',
                    map[position.first][position.second + 1] == 'O'
                )
            }

            Direction.LEFT -> {
                Pair(
                    map[position.first][position.second - 1] == '#',
                    map[position.first][position.second - 1] == 'O'
                )
            }

            Direction.UP -> {
                Pair(
                    map[position.first - 1][position.second] == '#',
                    map[position.first - 1][position.second] == 'O'
                )
            }

            Direction.DOWN -> {
                Pair(
                    map[position.first + 1][position.second] == '#',
                    map[position.first + 1][position.second] == 'O'
                )
            }
        }
    }

    private fun wallOrBigBoxInTheWay(
        map: List<CharArray>,
        position: Pair<Int, Int>,
        direction: Direction
    ): Triple<Boolean, Boolean, Boolean> {
        val nextChar: Char
        when (direction) {
            Direction.RIGHT -> {
                nextChar = map[position.first][position.second + 1]
            }

            Direction.LEFT -> {
                nextChar = map[position.first][position.second - 1]
            }

            Direction.UP -> {
                nextChar = map[position.first - 1][position.second]
            }

            Direction.DOWN -> {
                nextChar = map[position.first + 1][position.second]
            }
        }
        return Triple(nextChar == '#', nextChar == '[', nextChar == ']')
    }

    private fun readData(): Pair<List<CharArray>, String> {
        val parts = File("src/resources/Day15").readText().split("\n\n")
        return Pair(parts[0].lines().map { it.toCharArray() }, parts[1].replace("\n", ""))
    }
}