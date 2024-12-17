package utils

import java.util.*

fun bidirectionalAStar(grid: Array<Array<Char>>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    val directions = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)

    val forwardQueue = PriorityQueue<State>(compareBy { it.cost + heuristic(it.x, it.y, end) })
    val backwardQueue = PriorityQueue<State>(compareBy { it.cost + heuristic(it.x, it.y, start) })

    val forwardDistances = mutableMapOf<Triple<Int, Int, Direction>, Int>()
    val backwardDistances = mutableMapOf<Triple<Int, Int, Direction>, Int>()

    val forwardParentMap = mutableMapOf<Triple<Int, Int, Direction>, Triple<Int, Int, Direction>>()
    val backwardParentMap = mutableMapOf<Triple<Int, Int, Direction>, Triple<Int, Int, Direction>>()

    val initialDirection = Direction.LEFT
    var bestCost = Int.MAX_VALUE
    var meetingPoint: Pair<Int, Int>? = null // Track meeting point

    // Initialize forwardQueue with all possible directions
    for (d in directions) {
        val (nextX, nextY) = getNextPosition(start.first, start.second, d)
        if (isValid(grid, nextX, nextY)) {
            val turnCost = if (d == initialDirection) 0 else 1000
            forwardQueue.add(State(start.first, start.second, d, turnCost))
            forwardDistances[Triple(start.first, start.second, d)] = turnCost
        }
    }

    // Initialize backwardQueue
    for (d in directions) {
        backwardQueue.add(State(end.first, end.second, d, 0))
        backwardDistances[Triple(end.first, end.second, d)] = 0
    }

    while (forwardQueue.isNotEmpty() && backwardQueue.isNotEmpty()) {
        // Expand forward search
        val (newBestCost, forwardMeetingPoint) = expandSearch(
            forwardQueue, forwardDistances, backwardDistances, forwardParentMap, grid, directions, bestCost
        )
        if (forwardMeetingPoint != null) meetingPoint = forwardMeetingPoint
        bestCost = newBestCost

        // Expand backward search
        val (newBackwardCost, backwardMeetingPoint) = expandSearch(
            backwardQueue, backwardDistances, forwardDistances, backwardParentMap, grid, directions, bestCost
        )
        if (backwardMeetingPoint != null) meetingPoint = backwardMeetingPoint
        bestCost = newBackwardCost
    }

    // Ensure a valid meeting point was found
    if (meetingPoint == null) return -1

    // Count unique tiles in all shortest paths
    val uniqueTileCount = countUniqueTiles(forwardParentMap, backwardParentMap, meetingPoint)
    println("Unique tiles in shortest paths: $uniqueTileCount")
    println("Number of unique tiles in shortest paths: ${uniqueTileCount.size}")

    return bestCost
}

fun countUniqueTiles(
    forwardParentMap: Map<Triple<Int, Int, Direction>, Triple<Int, Int, Direction>>,
    backwardParentMap: Map<Triple<Int, Int, Direction>, Triple<Int, Int, Direction>>,
    meetingPoint: Pair<Int, Int>
): Set<Pair<Int, Int>> {
    val uniqueTiles = mutableSetOf<Pair<Int, Int>>() // To store all unique tiles
    val visitedStates = mutableSetOf<Triple<Int, Int, Direction>>() // Track visited states
    val queue = ArrayDeque<Triple<Int, Int, Direction>>() // BFS queue

    // Start BFS from all directions at the meeting point
    Direction.entries.forEach { dir ->
        val state = Triple(meetingPoint.first, meetingPoint.second, dir)
        if (forwardParentMap.containsKey(state) || backwardParentMap.containsKey(state)) {
            queue.add(state)
            visitedStates.add(state)
            uniqueTiles.add(Pair(meetingPoint.first, meetingPoint.second)) // Add the meeting tile
        }
    }

    // BFS to collect all unique tiles contributing to shortest paths
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        val currentTile = Pair(current.first, current.second)

        // Add tile to unique set
        uniqueTiles.add(currentTile)

        // Explore parents in forward parent map
        forwardParentMap[current]?.let { parent ->
            if (parent !in visitedStates) {
                queue.add(parent)
                visitedStates.add(parent)
            }
        }

        // Explore parents in backward parent map
        backwardParentMap[current]?.let { parent ->
            if (parent !in visitedStates) {
                queue.add(parent)
                visitedStates.add(parent)
            }
        }
    }

    return uniqueTiles
}



fun isValid(grid: Array<Array<Char>>, x: Int, y: Int): Boolean {
    return x in grid.indices && y in grid[0].indices && grid[x][y] != '#'
}

// Get next position based on direction
fun getNextPosition(x: Int, y: Int, direction: Direction): Pair<Int, Int> {
    return when (direction) {
        Direction.UP -> Pair(x - 1, y)
        Direction.DOWN -> Pair(x + 1, y)
        Direction.LEFT -> Pair(x, y - 1)
        Direction.RIGHT -> Pair(x, y + 1)
    }
}
fun expandSearch(
    queue: PriorityQueue<State>,
    distances: MutableMap<Triple<Int, Int, Direction>, Int>,
    otherDistances: MutableMap<Triple<Int, Int, Direction>, Int>,
    parentMap: MutableMap<Triple<Int, Int, Direction>, Triple<Int, Int, Direction>>,
    grid: Array<Array<Char>>,
    directions: List<Direction>,
    bestCost: Int
): Pair<Int, Pair<Int, Int>?> {
    var bestMeetingPoint: Pair<Int, Int>? = null
    val current = queue.poll()

    for (d in directions) {
        val (newX, newY) = getNextPosition(current.x, current.y, d)

        // Validate position: Is it a valid move in the grid?
        if (!isValid(grid, newX, newY)) continue

        val turnCost = if (current.direction != d) 1000 else 0
        val newCost = current.cost + turnCost + 1
        val stateKey = Triple(newX, newY, d)

        // Check if this state improves the cost and is not already visited
        if (newCost < distances.getOrDefault(stateKey, Int.MAX_VALUE)) {
            distances[stateKey] = newCost
            parentMap[stateKey] = Triple(current.x, current.y, current.direction) // Track parent
            queue.add(State(newX, newY, d, newCost))

            // Validate against backward search for meeting point
            val positionKey = Pair(newX, newY)
            val matchingKeys = otherDistances.filterKeys { it.first == positionKey.first && it.second == positionKey.second }

            for ((otherKey, backwardCost) in matchingKeys) {
                val alignTurnCost = if (d != otherKey.third) 1000 else 0
                val combinedCost = newCost + backwardCost + alignTurnCost

                if (combinedCost < bestCost) {
                    println("New best meeting point: ($newX, $newY), Combined cost: $combinedCost")
                    bestMeetingPoint = positionKey
                    return Pair(combinedCost, bestMeetingPoint)
                }
            }
        }
    }
    return Pair(bestCost, bestMeetingPoint)
}


fun heuristic(x: Int, y: Int, target: Pair<Int, Int>): Int {
    return Math.abs(x - target.first) + Math.abs(y - target.second)
}

data class State(
    val x: Int,             // Current row
    val y: Int,             // Current column
    val direction: Direction, // Current direction (UP, DOWN, LEFT, RIGHT)
    val cost: Int           // Total cost to reach this state
)
