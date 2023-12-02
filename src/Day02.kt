import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test


class Day02 {

    private val day = "Day02"

    @Test
    fun testSolution1() {

        val pick = Pick(nbBlue = 14, nbRed = 12, nbGreen = 13)

        assertThat(solution1(readInput("${day}_test"), pick)).isEqualTo(8)
    }

    @Test
    fun solution1() {
        val pick = Pick(nbBlue = 14, nbRed = 12, nbGreen = 13)

        assertThat(solution1(readInput("$day"), pick)).isEqualTo(3059)
    }

    @Test
    fun testSolution2() {
        assertThat(solution2(readInput("${day}_test"))).isEqualTo(2286)
    }

    @Test
    fun solution2() {
         assertThat(solution2(readInput(day))).isEqualTo(65371)
    }


    private data class Game(val id: Int, val picks: List<Pick>) {
        fun isPossible(target: Pick): Boolean {
            return picks.all { pick ->
                pick.isPossible(target)
            }
        }
    }

    private data class Pick(val nbBlue: Int, val nbRed: Int, val nbGreen: Int) {

        fun isPossible(target: Pick): Boolean {
            return nbBlue <= target.nbBlue && nbRed <= target.nbRed && nbGreen <= target.nbGreen
        }
    }

    private fun solution1(input: List<String>, target: Pick): Int {
        val games = readGames(input)

        var count = 0
        games.forEach { game ->
            if (game.isPossible(target)) {
                count += game.id
            }
        }

        return count
    }

    private fun solution2(input: List<String>): Int {
        val games = readGames(input)

        return games.sumOf { game ->
            val minNbBlue = game.floorOf { it.nbBlue }
            val minNbRed = game.floorOf { it.nbRed }
            val minNbGreen = game.floorOf { it.nbGreen }

            minNbBlue * minNbRed * minNbGreen
        }
    }

    private fun Game.floorOf(block: (Pick) -> Int): Int {
        return picks.maxOf { block(it) }
    }

    private fun readGames(input: List<String>): List<Game> {
        return input.map { string ->
            val split = string.split(":")

            val id = split.first().substring(5, split.first().length).toInt()

            val values = split[1].split(";")

            val picks = values.map { value ->
                var nbBlue: Int = 0
                var nbRed: Int = 0
                var nbGreen: Int = 0

                val pick = value.split(",")
                pick.forEach {
                    val onePick = it.trim()
                    if (onePick.contains("blue")) {
                        require(nbBlue == 0)
                        nbBlue = onePick.substring(0, onePick.indexOf(" ")).toInt()
                    } else if (onePick.contains("red")) {
                        require(nbRed == 0)
                        nbRed = onePick.substring(0, onePick.indexOf(" ")).toInt()
                    } else if (onePick.contains("green")) {
                        require(nbGreen == 0)
                        nbGreen = onePick.substring(0, onePick.indexOf(" ")).toInt()
                    } else {
                        TODO("unknown color:$onePick")
                    }
                }
                Pick(nbBlue, nbRed, nbGreen)
            }

            Game(id, picks)
        }
    }


}
