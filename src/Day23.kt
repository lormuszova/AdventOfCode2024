import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        Day23().partOne()
    }
    println("Time: $time")
}

class Day23 {
    fun partOne() {
        val (mapOfPairs, mapOfPairsInverse, bidirectionalMap) = readData()
        println("Map $mapOfPairs")
        val part1 = processMapOfConnectedComputers(mapOfPairs, mapOfPairsInverse)
        println("Part 1: {${part1.size}}")

        val largestClique = findLargestCliqueWithPivot(bidirectionalMap)
        println("Part 2: Largest Clique Size = ${largestClique.size}")
        println("Members: ${largestClique.sorted().joinToString(",")}")
    }


    fun processMapOfConnectedComputers(
        mapOfPairs: Map<String, Set<String>>,
        mapOfPairsInverse: Map<String, Set<String>>
    ): Set<String> {
        val results = mutableSetOf<String>()
        for ((key, computers) in mapOfPairs) {
            val keySet = mapOfPairs[key] ?: emptySet()
            for (computer in computers) {
                val computerSet = mapOfPairs[computer] ?: emptySet()
                for (secondComputer in computerSet) {
                    if (secondComputer in keySet || secondComputer in mapOfPairsInverse[key].orEmpty()) {
                        val resultTemp = sortedSetOf(key, computer, secondComputer)
                        if (resultTemp.any { it.first() == 't' })
                            results.add(resultTemp.joinToString("-"))
                    }
                }
            }
        }
        return results
    }

    /**
     * Finds the largest clique in the graph using the Bron–Kerbosch algorithm with pivoting.
     *
     * @param bidirectionalMap The bidirectional adjacency map representing the graph.
     * @return The largest clique as a set of computer names.
     */
    fun findLargestCliqueWithPivot(bidirectionalMap: Map<String, Set<String>>): Set<String> {
        val cliques = mutableSetOf<Set<String>>()
        val allNodes = bidirectionalMap.keys.toSet()

        bronKerboschWithPivot(emptySet(), emptySet(), allNodes, bidirectionalMap, cliques)

        // Find the largest clique
        return cliques.maxByOrNull { it.size } ?: emptySet()
    }

    /**
     * Bron–Kerbosch recursive algorithm with pivot to find maximal cliques.
     *
     * @param potentialClique The current growing clique.
     * @param alreadyFound Nodes already included in the clique.
     * @param candidates Nodes that can potentially be added to the clique.
     * @param adjacencyMap The bidirectional adjacency map.
     * @param cliques The set to store all found maximal cliques.
     */
    private fun bronKerboschWithPivot(
        potentialClique: Set<String>,
        alreadyFound: Set<String>,
        candidates: Set<String>,
        adjacencyMap: Map<String, Set<String>>,
        cliques: MutableSet<Set<String>>
    ) {
        if (candidates.isEmpty() && alreadyFound.isEmpty()) {
            // Found a maximal clique
            cliques.add(potentialClique)
            return
        }

        // Choose a pivot node from candidates ∪ alreadyFound with the maximum degree
        val pivot = (candidates + alreadyFound).maxByOrNull { (adjacencyMap[it]?.size) ?: 0 }
        val pivotNeighbors = pivot?.let { adjacencyMap[it] } ?: emptySet()
        val nonNeighbors = candidates - pivotNeighbors

        for (node in nonNeighbors.toList()) {
            val neighbors = adjacencyMap[node] ?: emptySet()
            bronKerboschWithPivot(
                potentialClique + node,
                alreadyFound.intersect(neighbors),
                candidates.intersect(neighbors),
                adjacencyMap,
                cliques
            )
            // Move node from candidates to alreadyFound
            // This is implicitly handled by the recursion
        }
    }

    private fun readData(): Triple<Map<String, Set<String>>, Map<String, Set<String>>, Map<String, Set<String>>> {
        val file = File("src/resources/Day23").readLines()
        val maps = mutableMapOf<String, MutableSet<String>>()
        val mapsInverse = mutableMapOf<String, MutableSet<String>>()
        val bidirectionalMap = mutableMapOf<String, MutableSet<String>>()
        file.forEach {
            val parts = it.split("-")
            val key = parts[0]
            val value = parts[1]
            maps.computeIfAbsent(key) { mutableSetOf<String>() }.add(value)
            mapsInverse.computeIfAbsent(value) { mutableSetOf<String>() }.add(key)
            bidirectionalMap.computeIfAbsent(key) { mutableSetOf<String>() }.add(value)
            bidirectionalMap.computeIfAbsent(value) { mutableSetOf<String>() }.add(key)
        }
        return Triple(maps, mapsInverse, bidirectionalMap)
    }
}