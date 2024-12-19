import java.io.File

fun main() {
    Day19().apply {
        val (patterns, designs) = readData()
        var countOfCreatedDesigns = 0
        var countOfWaysToCreateDesigns = 0L
        designs.forEach {
            if (canDesignBeCreated(patterns, it)) {
                countOfCreatedDesigns++
            }
            countOfWaysToCreateDesigns += countWaysDesignBeCreated(patterns, it)
        }
        println("Part 1: Possible designs: $countOfCreatedDesigns")
        println("Part 2: Ways to create designs: $countOfWaysToCreateDesigns")
    }
}

class Day19 {
    fun canDesignBeCreated(patterns: Map<Char, List<String>>, design: String): Boolean {
        val canBuild = BooleanArray(design.length + 1) { false }
        canBuild[0] = true
        for (i in design.indices) {
            if (!canBuild[i]) continue
            patterns[design[i]]?.forEach { pattern ->
                if (i + pattern.length <= design.length && design.startsWith(pattern, i)) {
                    canBuild[i + pattern.length] = true
                }
            }
        }
        return canBuild[design.length]
    }

    fun countWaysDesignBeCreated(patterns: Map<Char, List<String>>, design: String): Long {
        val ways = LongArray(design.length + 1) { 0L }
        ways[0] = 1
        for (i in design.indices) {
            if (ways[i] == 0L) continue
            patterns[design[i]]?.forEach { pattern ->
                if (i + pattern.length <= design.length && design.startsWith(pattern, i)) {
                    ways[i + pattern.length] += ways[i]
                }
            }
        }
        return ways[design.length]
    }

    fun readData(): Pair<Map<Char, List<String>>, List<String>> {
        val fileName = "src/resources/Day19"
        val file = File(fileName).readText().split("\n\n")
        val patterns = file[0].split(", ").groupBy { it[0] }
        val designs = file[1].split("\n")
        return patterns to designs
    }
}