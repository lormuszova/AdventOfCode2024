package utils

// Coordinates - row, column
enum class Direction(val coordinates: Pair<Int, Int>) {
    RIGHT (0 to 1),
    UP (1 to 0),
    LEFT (0 to -1),
    DOWN (-1 to 0)
}