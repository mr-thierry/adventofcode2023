import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.util.*


class Day01 {

    private val day = "Day01"

    private val deadEnds = mutableListOf<Point>()

    @Test
    fun testSolution1() {
        assertThat(solution1(readInput("${day}_test"))).isEqualTo(31)
    }

    @Test
    fun solution1() {
        assertThat(solution1(readInput(day))).isEqualTo(408)
    }

    @Test
    fun testSolution2() {
        assertThat(solution2(readInput("${day}_test"))).isEqualTo(29)
    }

    @Test
    fun solution2() {
        assertThat(solution2(readInput(day))).isEqualTo(399)
    }

    private fun solution1(input: List<String>): Int {
        val routes = readRoutesFromStart(input)
        return findShortestRoute(routes, input)
    }

    private fun solution2(input: List<String>): Int {
        val routes = readRoutesFromLowest(input)
        return findShortestRoute(routes, input)
    }

    private fun findShortestRoute(routes: Queue<Route>, map: List<String>): Int {
        val visited = mutableSetOf<Point>()
        val maxX = map[0].toCharArray().size

        while (routes.isNotEmpty()) {
            val route = routes.poll()

            val currentPosition = Point(route.dx, route.dy)
            if (!visited.contains(currentPosition)) {
                visited.add(currentPosition)

                if (map[route.dy][route.dx] == 'E') {
                    return route.size
                }

                //left
                if (route.dx > 0) {
                    walkToNextPosition(route, -1, 0, map, routes)
                }

                //top
                if (route.dy > 0) {
                    walkToNextPosition(route, 0, -1, map, routes)
                }

                //right
                if (route.dx < maxX - 1) {
                    walkToNextPosition(route, +1, 0, map, routes)
                }

                //bottom
                if (route.dy < map.size - 1) {
                    walkToNextPosition(route, 0, +1, map, routes)
                }
            }
        }

        error("Cannot find route")
    }

    private fun walkToNextPosition(
        route: Route,
        dx: Int,
        dy: Int,
        map: List<String>,
        routes: Queue<Route>
    ) {
        val nextPositionX = route.dx + dx
        val nextPositionY = route.dy + dy

        val currentHeight = map[route.dy][route.dx]
        val nextHeight = map[nextPositionY][nextPositionX]

        if (height(nextHeight) - height(currentHeight) < 2) {
            routes.add(Route(nextPositionX, nextPositionY, route.size + 1))
        }
    }

    private fun readRoutesFromStart(input: List<String>): Queue<Route> {
        for (y in input.indices) {
            for (x in input[0].indices) {
                if (input[y][x] == 'S') {
                    val routes = LinkedList<Route>()
                    routes.add(Route(x, y, 0))
                    return routes
                }
            }
        }

        error("Cannot find routes from start")
    }


    private fun readRoutesFromLowest(input: List<String>): Queue<Route> {
        val routes = LinkedList<Route>()

        for (y in input.indices) {
            for (x in input[0].indices) {
                if (height(input[y][x]) == 'a') {
                    routes.add(Route(x, y, 0))
                }
            }
        }

        return routes
    }

    class Route(val dx: Int, val dy: Int, val size: Int)

    data class Point(val x: Int, val y: Int)

    private fun height(ch: Char) = when (ch) {
        'S' -> 'a'
        'E' -> 'z'
        else -> ch
    }


}
