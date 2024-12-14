import java.io.File

fun main() {
    Day14().apply{
        partOne()
        partTwo()
    }
}

class Day14 {
    private val totalHeight = 103
    private val totalWidth = 101
    private val quadrants = initializeQuadrants(totalWidth, totalHeight)

    private val safetyFactor = mutableMapOf<Quadrant, Int>().apply {
        quadrants.values.forEach { this[it] = 0 }
    }

    fun partTwo() {
        val robots = readData()
        val moves = 100000
        var map = MutableList(totalHeight) { MutableList(totalWidth) { '.' } }

        for (second in 1..moves) {
            robots.forEach { robot ->
                val endCoordinates = moveRobotCoordinates(robot, second)
                map[endCoordinates.second][endCoordinates.first] = '#'
            }

            File("output").mkdirs()
            if (map.any { row -> row.joinToString(" ").contains("# # # # # # # # # # # # # # # # # # # # #") }) {
                File("output/MapAtSecond.txt").writeText("Map at second $second:\n")
                map.forEach { row -> File("output/MapAtSecond.txt").appendText(row.joinToString(" ") + "\n") }
                break
            }
            map = MutableList(totalHeight) { MutableList(totalWidth) { '.' } }
        }
    }

    fun partOne() {
        val robots = readData()
        val moves = 100
        robots.forEach {
            val endQuadrant = moveRobot(it, moves)
            if (endQuadrant != null) {
                println("Robot is in $endQuadrant")
                safetyFactor[endQuadrant] = safetyFactor[endQuadrant]!! + 1
            } else {
                println("Robot is out of bounds")
            }
        }
        println("Safety factor: $safetyFactor")
        println("Safety factor: ${safetyFactor.values.reduce { acc, i -> acc * i }}")
    }

    private fun readData(): List<Pair<Pair<Int, Int>, Pair<Int, Int>>>{
        return File("src/resources/Day14").readLines().map { line ->
            val (x1, y1, x2, y2) = line.filter { it.isDigit() || it == ',' || it == ' ' || it == '-' }.split(",", " ").map { it.toInt() }
            Pair(Pair(x1, y1), Pair(x2, y2))
        }
    }

    data class Quadrant(val x: IntRange, val y: IntRange)

    private fun initializeQuadrants(totalWidth: Int, totalHeight: Int): Map<String, Quadrant> {
        return mapOf(
            "TOP_LEFT" to Quadrant(IntRange(0, totalWidth / 2 - 1), IntRange(0, totalHeight / 2 - 1)),
            "TOP_RIGHT" to Quadrant(
                IntRange(totalWidth / 2 + 1, totalWidth),
                IntRange(0, totalHeight / 2 - 1)
            ),
            "BOTTOM_LEFT" to Quadrant(
                IntRange(0, totalWidth / 2 - 1),
                IntRange(totalHeight / 2 + 1, totalHeight)
            ),
            "BOTTOM_RIGHT" to Quadrant(
                IntRange(totalWidth / 2 + 1, totalWidth),
                IntRange(totalHeight / 2 + 1, totalHeight)
            )
        )
    }

    private fun isInside(x: Int, y: Int): Quadrant? {
        return quadrants.values.firstOrNull { quadrant ->
            x in quadrant.x && y in quadrant.y
        }
    }

    private fun robotInRange(coordinates: Pair<Int, Int>): Boolean {
        val (x, y) = coordinates

        return x in 0..<totalWidth && y in 0..<totalHeight
    }

    private fun moveRobot(robot: Pair<Pair<Int, Int>, Pair<Int, Int>>, moves: Int): Quadrant? {
        val (start, velocity) = robot
        var end = Pair(start.first + velocity.first * moves, start.second + velocity.second * moves)
        println("endTotal: $end")
        while (!robotInRange(end)) {
            end = Pair(
                (end.first + totalWidth) % totalWidth,
                (end.second + totalHeight) % totalHeight
            )
            if (end.first < 0) end = end.copy(first = end.first + totalWidth)
            if (end.second < 0) end = end.copy(second = end.second + totalHeight)
        }
        val endInRange = Pair(end.first % totalWidth, end.second % totalHeight)
        println("endInRange: $endInRange")
        return isInside(endInRange.first, endInRange.second)

    }

    private fun moveRobotCoordinates(robot: Pair<Pair<Int, Int>, Pair<Int, Int>>, moves: Int): Pair<Int, Int> {
        val (start, velocity) = robot
        var end = Pair(start.first + velocity.first * moves, start.second + velocity.second * moves)
        while (!robotInRange(end)) {
            end = Pair(
                (end.first + totalWidth) % totalWidth,
                (end.second + totalHeight) % totalHeight
            )
            if (end.first < 0) end = end.copy(first = end.first + totalWidth)
            if (end.second < 0) end = end.copy(second = end.second + totalHeight)
        }
        val endInRange = Pair(end.first % totalWidth, end.second % totalHeight)
        return Pair(endInRange.first, endInRange.second)

    }
}

