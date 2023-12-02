import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test


class Day01 {

    private val day = "Day01"

    @Test
    fun testSolution1() {
        assertThat(solution1(readInput("${day}_test"))).isEqualTo(142)
    }

    @Test
    fun solution1() {
        assertThat(solution1(readInput(day))).isEqualTo(54239)
    }

    @Test
    fun testSolution2() {
        assertThat(solution2(readInput("${day}_test2"))).isEqualTo(281)
    }

    @Test
    fun solution2() {
        assertThat(solution2(readInput(day))).isEqualTo(55343)
    }

    private fun solution1(input: List<String>): Int {
        return input.sumOf {
            "${it[it.indexOfFirst { it.isDigit() }]}${it[it.indexOfLast { it.isDigit() }]}".toInt()
        }
    }

    private val numbersAsString = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    private fun solution2(input: List<String>): Int {
        return input.sumOf { string ->

            val firstNumber = numbersAsString
                .mapNotNull { number -> if (string.contains(number)) number else null }
                .minByOrNull { number -> string.indexOf(number) }

            val firstDigitIdx = string.indexOfFirst { it.isDigit() }.takeIf { it > -1 } ?: Int.MAX_VALUE
            val firstNumberIdx = firstNumber?.let { string.indexOf(firstNumber) }?.takeIf { it > -1 } ?: Int.MAX_VALUE

            val firstNumberFromString = if (firstDigitIdx < firstNumberIdx) {
                string[firstDigitIdx].toString().toInt()
            } else {
                numbersAsString.indexOf(firstNumber) + 1
            }

            val lastNumber = numbersAsString
                .mapNotNull { number -> if (string.contains(number)) number else null }
                .maxByOrNull { number -> string.lastIndexOf(number) }

            val lastDigitIdx = string.indexOfLast { it.isDigit() }.takeIf { it > -1 } ?: -1
            val lastNumberIdx = lastNumber?.let { string.lastIndexOf(lastNumber) }?.takeIf { it > -1 } ?: -1

            val lastNumberFromString = if (lastDigitIdx > lastNumberIdx) {
                string[lastDigitIdx].toString().toInt()
            } else {
                numbersAsString.indexOf(lastNumber) + 1
            }

            val rsult = "$firstNumberFromString$lastNumberFromString".toInt()
            rsult
        }
    }


}
