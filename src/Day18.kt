import java.io.File
import java.util.*

fun main() {
    Day18().partOne()
    Day18().partTwo()
}

class Day18 {
    fun partOne() {
        val gridSize = 70
        val walls = readData().subList(0, 1023).toSet()

        println(bidirectionalBFS(walls, 0 to 0, gridSize to gridSize))
    }

    fun partTwo() {
        val gridSize = 70
        val walls = readData()
        val result = findProblematicWall(walls, 0 to 0, gridSize to gridSize)
        println("Problematic wall $result.")
    }

     private fun findProblematicWall(walls: List<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Pair<Int, Int>? {
        var left = 0
        var right = walls.size - 1
        var problematicWall: Pair<Int, Int>? = null

        while (left <= right) {
            val mid = (left + right) / 2
            val currentWalls = walls.subList(0, mid + 1).toSet()

            if (bidirectionalBFS(currentWalls, start, end) == -1) {
                problematicWall = walls[mid]
                right = mid - 1
            } else {
                left = mid + 1
            }
        }
        return problematicWall
    }



    private fun bidirectionalBFS(walls: Set<Pair<Int, Int>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        if (start == end) return 0

        val directions = listOf(
            Pair(-1, 0), // Up
            Pair(1, 0),  // Down
            Pair(0, -1), // Left
            Pair(0, 1)   // Right
        )

        val maxSize = maxOf(end.first, end.second) // Maximum row/column size

        val forwardQueue = ArrayDeque<Pair<Pair<Int, Int>, Int>>() // Position, steps
        val backwardQueue = ArrayDeque<Pair<Pair<Int, Int>, Int>>() // Position, steps

        val forwardVisited = mutableMapOf<Pair<Int, Int>, Int>() // Position -> Steps
        val backwardVisited = mutableMapOf<Pair<Int, Int>, Int>() // Position -> Steps

        forwardQueue.add(start to 0)
        backwardQueue.add(end to 0)

        forwardVisited[start] = 0
        backwardVisited[end] = 0

        while (forwardQueue.isNotEmpty() && backwardQueue.isNotEmpty()) {

            // Expand forward search
            val forwardResult = expandBFS(forwardQueue, forwardVisited, backwardVisited, walls, directions, maxSize)
            if (forwardResult != -1) return forwardResult

            // Expand backward search
            val backwardResult = expandBFS(backwardQueue, backwardVisited, forwardVisited, walls, directions, maxSize)
            if (backwardResult != -1) return backwardResult
        }

        return -1 // No path found
    }


    private fun expandBFS(
        queue: ArrayDeque<Pair<Pair<Int, Int>, Int>>,
        visited: MutableMap<Pair<Int, Int>, Int>,
        otherVisited: MutableMap<Pair<Int, Int>, Int>,
        walls: Set<Pair<Int, Int>>,
        directions: List<Pair<Int, Int>>,
        maxSize: Int
    ): Int {
        if (queue.isEmpty()) {
            return -1
        }
        val (current, steps) = queue.removeFirst()
        for ((dx, dy) in directions) {
            val next = current.first + dx to current.second + dy

            // Boundary check
            if (next.first !in 0..maxSize || next.second !in 0..maxSize) {
                continue
            }
            if (next in walls) {
                continue
            }
            if (next in visited) {
                continue
            }
            if (next in otherVisited) {
                return steps + 1 + otherVisited[next]!!
            }
            visited[next] = steps + 1
            queue.add(next to steps + 1)
        }
        return -1
    }


    private fun readData(): List<Pair<Int, Int>> {
        val fileName = "src/resources/Day18"
        return File(fileName).readLines().map { it.split(",").let { it[1].toInt() to it[0].toInt() } }
    }
}