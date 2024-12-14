import java.io.File

fun main() {
    val day13 = Day13()
    day13.partOne()
    day13.partTwo()
}

class Day13 {
    fun partOne() {
        val prizes = readData().split("\n\n")
        var price = 0
        prizes.forEach { prizeData ->
            val (a, b, prize) = parseLine(prizeData)
            val result = solveEquationInt(a, b, prize)
            result?.let {
                price += result.first * 3 + result.second
            }
        }
        println("Price: $price")
    }

    fun partTwo() {
        val prizes = readData().split("\n\n")
        var price = 0L
        prizes.forEach { prizeData ->
            val (a, b, prize) = parseLine(prizeData)
            val fixedPrize = Pair(prize.first + 10000000000000, prize.second + 10000000000000)
            val result = solveEquation(a, b, fixedPrize)
            result?.let {
                price += result.first * 3L + result.second
            }
        }
        println("Price: $price")
    }

    private fun readData() : String {
        val fileName = "src/resources/Day13"
        return File(fileName).readText()
    }

    private fun parseLine(line: String): Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>> {
        val regex = """Button A: X\+(\d+), Y\+(\d+)\s*Button B: X\+(\d+), Y\+(\d+)\s*Prize: X=(\d+), Y=(\d+)""".toRegex()
        val matchResult = regex.find(line)
        val (ax, ay, bx, by, px, py) = matchResult!!.destructured
        val A = Pair(ax.toInt(), ay.toInt())
        val B = Pair(bx.toInt(), by.toInt())
        val Prize = Pair(px.toInt(), py.toInt())
        return Triple(A, B, Prize)
    }

    private fun solveEquationInt(A: Pair<Int, Int>, B: Pair<Int, Int>, Prize: Pair<Int, Int>): Pair<Int, Int>? {
        return solveEquation(A, B, Prize.toLongPair())?.toIntPair()
    }

    private fun Pair<Int, Int>.toLongPair(): Pair<Long, Long> = Pair(this.first.toLong(), this.second.toLong())
    private fun Pair<Long, Long>.toIntPair(): Pair<Int, Int> = Pair(this.first.toInt(), this.second.toInt())

    private fun solveEquation(A: Pair<Int, Int>, B: Pair<Int, Int>, Prize: Pair<Long, Long>): Pair<Long, Long>? {
        val determinant = (A.first * B.second) - (B.first * A.second)
        var x = (Prize.first * B.second) - (B.first * Prize.second)
        if (x % determinant == 0L) {
            x /= determinant
        } else {
            return null
        }
        var y = (A.first * Prize.second) - (Prize.first * A.second)
        if (y % determinant == 0L) {
            y /= determinant
        } else {
            return null
        }
        return Pair(x, y)
    }
}