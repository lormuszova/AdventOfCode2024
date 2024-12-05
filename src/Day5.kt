import java.io.File

fun main() {
    firstPart()
    secondPart()
}

private fun readPairs(): Map<Int, Array<Int>>{
    val fileName = "src/resources/Day5Pages"
    val file = File(fileName).readLines()
    val map = mutableMapOf<Int, Array<Int>>()
    file.map {
        val pair = it.split("|")
        val firstValue = pair[0].toInt()
        val secondValue = pair[1].toInt()
        if (map.containsKey(firstValue)) {
            map[firstValue] = map[firstValue]?.plus(secondValue) ?: arrayOf(secondValue)
        } else {
            map[firstValue] = arrayOf(secondValue)
        }
        if (map.containsKey(secondValue)) {
            map[secondValue] = map[secondValue]?.plus(-firstValue) ?: arrayOf(-firstValue)
        } else {
            map[secondValue] = arrayOf(-firstValue)
        }
    }
    return map
}

private fun readOrders(): List<List<Int>> {
    val fileName = "src/resources/Day5PrintingOrder"
    val file = File(fileName).readLines()
    return file.map {
        it.split(",").map { numbers-> numbers.toInt() }
    }
}

private fun firstPart() {
    val pairs = readPairs()
    val orders = readOrders()
    var result = 0
    orders.forEach { order ->
        order.forEachIndexed() { index, number ->
            if (pairs.containsKey(number)) {
                for(i in 0..<index) {
                    if (pairs[number]!!.contains(order[i])) {
                        println("Order $order is not valid, because $number is not allowed to be after ${order[i]}")
                        return@forEach
                    }
                }
                for(i in index..<order.size) {
                    if (pairs[number]!!.contains(-order[i])) {
                        println("Order $order is not valid, because $number is not allowed to be before ${order[i]}")
                        return@forEach
                    }
                }
            }
        }
        result += order[order.size/2]
    }
    print("Result: $result")
}

private fun secondPart() {
    val pairs = readPairs()
    val orders = readOrders()
    var result = 0
    orders.forEach { order ->
        println("Verifying order: $order")
        val fixedOrder = verifyOrder(order.toTypedArray(), pairs)
        result += if(fixedOrder.isNotEmpty()) fixedOrder[fixedOrder.size/2] else 0
    }
    print("Result: $result")
}

private fun verifyOrder(order: Array<Int>, pairs: Map<Int, Array<Int>>): Array<Int> {
    var isIncorrectlyOrder = false
    order.forEachIndexed() { index, number ->
        if (pairs.containsKey(number)) {
            for(i in 0..<index) {
                if (pairs[number]!!.contains(order[i])) {
                    val temp = order[index]
                    order[index] = order[i]
                    order[i] = temp
                    isIncorrectlyOrder = true
                }
            }
            for(i in index..<order.size) {
                if (pairs[number]!!.contains(-order[i])) {
                    val temp = order[index]
                    order[index] = order[i]
                    order[i] = temp
                    isIncorrectlyOrder = true

                }
            }
            if(isIncorrectlyOrder) verifyOrder(order, pairs)
        }
    }
    return if(isIncorrectlyOrder) order else arrayOf()
}