import utils.Direction
import java.io.File

/**
 * Main function to start the program.
 */
fun main() {
    partOne()
    partTwo()
}

private fun partOne() {
    val regions = createMapOfRegions()
    var countOfRegions = 0
    var countOfBoundaries = 0
    var totalPrice = 0
    regions.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { charIndex, char ->
            if(char != '0') {
                val (count, boundaries) = floodFill(regions, lineIndex, charIndex)
                println("Region $char has $count elements and $boundaries boundaries.")
                countOfRegions += count
                countOfBoundaries += boundaries
                totalPrice += count * boundaries
            }
        }
    }
    println("Total price of fencing: $totalPrice")
}

private fun partTwo() {
    val regions = createMapOfRegions()
    var countOfRegions = 0
    var countOfSides = 0
    var totalPrice = 0
    regions.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { charIndex, char ->
            if(char != '0') {
                val (count, sides) = floodFillSides(regions, lineIndex, charIndex)
                println("Region $char has $count elements and $sides sides.")
                countOfRegions += count
                countOfSides += sides
                totalPrice += count * sides
            }
        }
    }
    println("Total price of fencing: $totalPrice")
}

private fun createMapOfRegions(): MutableList<CharArray> {
    val file = File("src/resources/Day12").readLines().map { it.toCharArray() }
    return file.toMutableList()
}


private fun floodFill(regions: MutableList<CharArray>, lineIndex: Int, charIndex: Int): Pair<Int, Int> {
    val char = regions[lineIndex][charIndex]
    val visited = mutableSetOf<Pair<Int, Int>>()
    val queue = mutableListOf(Pair(lineIndex, charIndex))
    var count = 0
    var boundaries = 0

    val directions = listOf(
        Pair(0, 1),  // Right
        Pair(0, -1), // Left
        Pair(1, 0),  // Down
        Pair(-1, 0)  // Up
    )

    while (queue.isNotEmpty()) {
        val (currentLine, currentChar) = queue.removeAt(0)
        if (visited.contains(Pair(currentLine, currentChar))) continue

        visited.add(Pair(currentLine, currentChar))
        count++

        for ((dx, dy) in directions) {
            val newLineIndex = currentLine + dx
            val newCharIndex = currentChar + dy
            if (isWithinBounds(newLineIndex, newCharIndex, regions)) {
                if (regions[newLineIndex][newCharIndex] == char && !visited.contains(Pair(newLineIndex, newCharIndex))) {
                    queue.add(Pair(newLineIndex, newCharIndex))
                } else if (regions[newLineIndex][newCharIndex] != char) {
                    boundaries++
                }
            } else {
                boundaries++
            }
        }
    }

    visited.forEach { (line, char) ->
        regions[line][char] = '0'
    }

    return Pair(count, boundaries)
}

private fun floodFillSides(regions: MutableList<CharArray>, lineIndex: Int, charIndex: Int): Pair<Int, Int> {
    val char = regions[lineIndex][charIndex]
    val visited = mutableSetOf<Pair<Int, Int>>()
    val queue = mutableListOf(Pair(lineIndex, charIndex))
    var count = 0
    val sides = mutableMapOf<Direction, MutableList<Pair<Int, Int>>>()

    val directions = listOf(
        Pair(0, 1),  // Right
        Pair(0, -1), // Left
        Pair(1, 0),  // Down
        Pair(-1, 0)  // Up
    )

    while (queue.isNotEmpty()) {
        val (currentLine, currentChar) = queue.removeAt(0)
        if (visited.contains(Pair(currentLine, currentChar))) continue

        visited.add(Pair(currentLine, currentChar))
        count++

        for ((dx, dy) in directions) {
            val newLineIndex = currentLine + dx
            val newCharIndex = currentChar + dy
            if (isWithinBounds(newLineIndex, newCharIndex, regions)) {
                if (regions[newLineIndex][newCharIndex] == char && !visited.contains(
                        Pair(
                            newLineIndex,
                            newCharIndex
                        )
                    )
                ) {
                    queue.add(Pair(newLineIndex, newCharIndex))
                } else if (regions[newLineIndex][newCharIndex] != char) {
                    when (dx to dy) {
                        0 to 1 -> sides.getOrPut(Direction.RIGHT) { mutableListOf() }.apply {
                                add(Pair(currentLine, currentChar))
                                sortBy { it.first }
                            }

                        0 to -1 -> sides.getOrPut(Direction.LEFT) { mutableListOf() }.apply {
                            add(Pair(currentLine, currentChar))
                            sortBy { it.first }
                        }


                        1 to 0 -> sides.getOrPut(Direction.DOWN) { mutableListOf() }.apply {
                            add(Pair(currentLine, currentChar))
                            sortBy { it.second }
                        }

                        -1 to 0 -> sides.getOrPut(Direction.UP) { mutableListOf() }.apply {
                            add(Pair(currentLine, currentChar))
                            sortBy { it.second }
                        }
                    }
                }
            } else {
                when (dx to dy) {
                    0 to 1 -> sides.getOrPut(Direction.RIGHT) { mutableListOf() }.apply {
                        add(Pair(currentLine, currentChar))
                        sortBy { it.first }
                    }

                    0 to -1 -> sides.getOrPut(Direction.LEFT) { mutableListOf() }.apply {
                        add(Pair(currentLine, currentChar))
                        sortBy { it.first }
                    }


                    1 to 0 -> sides.getOrPut(Direction.DOWN) { mutableListOf() }.apply {
                        add(Pair(currentLine, currentChar))
                        sortBy { it.second }
                    }

                    -1 to 0 -> sides.getOrPut(Direction.UP) { mutableListOf() }.apply {
                        add(Pair(currentLine, currentChar))
                        sortBy { it.second }
                    }
                }
            }
        }
    }
            println("Sides before filter: $sides")
            sides.forEach { (direction, list) ->
                val iterator = list.iterator()
                while (iterator.hasNext()) {
                    val pair = iterator.next()
                    when (direction) {
                        Direction.LEFT -> {
                            if (list.contains(Pair(pair.first + 1, pair.second)) || list.contains(Pair(pair.first - 1, pair.second))) {
                                iterator.remove()
                            }
                        }
                        Direction.RIGHT -> {
                            if (list.contains(Pair(pair.first + 1, pair.second)) || list.contains(Pair(pair.first - 1, pair.second))) {
                                iterator.remove()
                            }
                        }
                        Direction.UP -> {
                            if (list.contains(Pair(pair.first, pair.second + 1)) || list.contains(Pair(pair.first, pair.second - 1))) {
                                iterator.remove()
                            }
                        }
                        Direction.DOWN -> {
                            if (list.contains(Pair(pair.first, pair.second + 1)) || list.contains(Pair(pair.first, pair.second - 1))) {
                                iterator.remove()
                            }
                        }
                    }
                }


    }

    visited.forEach { (line, char) ->
        regions[line][char] = '0'
    }
    println("Sides: $sides")

    return Pair(count, sides.values.sumOf { it.size })
}


private fun isWithinBounds(lineIndex: Int, charIndex: Int, file: List<CharArray>): Boolean {
    return lineIndex >= 0 && lineIndex < file.size && charIndex >= 0 && charIndex < file[lineIndex].size
}