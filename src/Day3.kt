import java.io.File

fun main() {
    findNumbers()
}

private fun readData(): String {
    val fileName = "src/resources/Day3"
    val file = File(fileName).readText()
    return file
}

private fun findNumbers() {
    val data = readData().replace("\n", " ").replace("do()", "\ndo()")
    val dos = Regex("don't\\(\\).*").replace(data, "XXX")
    println("dos: $dos")

    var result = 0L

    val mul = Regex("mul\\([0-9]+,[0-9]+\\)").findAll(dos)
    println("mul: $mul")
    mul.forEach { it ->
        val numbers = it.value.split(",").map { it.replace("mul(", "").replace(")", "").toInt() }
        result += numbers[0].toLong() * numbers[1].toLong()
        println("numbers: $numbers")
    }

    println("Result: $result")
}