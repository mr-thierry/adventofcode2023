import Item.*
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test


class Day03 {

    private val day = "Day03"

    @Test
    fun testSolution1() {
        assertThat(solution1(readInput("${day}_test"))).isEqualTo(4361)
    }

    @Test
    fun solution1() {
        assertThat(solution1(readInput("$day"))).isEqualTo(531932)
    }

    @Test
    fun testSolution2() {
        assertThat(solution2(readInput("${day}_test"))).isEqualTo(467835)
    }

    @Test
    fun solution2() {
        assertThat(solution2(readInput(day))).isEqualTo(73646890)
    }

    private fun solution2(input: List<String>): Int {
        val schematic = readSchematic(input)

        var totalCount  = 0
        schematic.forEachIndexed { row, line ->
            line.forEachIndexed { column, item ->
                if (item is ItemGear) {
                    val parts = getParts(row, column, schematic)
                    if (parts.size == 2) {
                        totalCount += (parts[0].value * parts[1].value)
                    }
                }
            }
        }

        return totalCount
    }

    private fun getParts(rowIndex: Int, colIndex: Int, schematic: List<List<Item>>): List<ItemIntStart> {
        val parts = mutableListOf<ItemIntStart>()

        if (rowIndex -1 >= 0) {
            val row = schematic[rowIndex-1]
            processRow(colIndex, row, parts)
        }

        val rowMiddle = schematic[rowIndex]
        processRow(colIndex, rowMiddle, parts)

        if (rowIndex +1 <= schematic.size) {
            val row = schematic[rowIndex+1]
            processRow(colIndex, row, parts)
        }

        return parts
    }

    private fun processRow(
        colIndex: Int,
        row: List<Item>,
        parts: MutableList<ItemIntStart>
    ) {
        //top row right
        if (colIndex + 1 < row.size) {
            val item = row[colIndex + 1]
            if (item is ItemIntStart) {
                parts.add(item)
            }
        }

        //top row center
        val itemCenter = row[colIndex]
        if (itemCenter is ItemIntStart) {
            parts.add(itemCenter)
        }

        //top row left
        if (colIndex - 1 >= 0) {
            val item = row[colIndex - 1]
            if (item is ItemIntStart) {
                parts.add(item)
            } else if (item is ItemIntRest) {
                parts.add(ItemIntStart(item.value))
            }
        }
    }

    private fun List<Item>.containsItemInt(startCol: Int, endCol: Int): Boolean {
        return  subList(startCol, endCol).any {
            it is ItemIntStart || it is ItemIntRest
        }
    }


    private fun solution1(input: List<String>): Int {
        val schematic = readSchematic(input)

        var totalCount  = 0
        schematic.forEachIndexed { row, line ->
            line.forEachIndexed { column, item ->
                if (item is ItemIntStart) {
                    if (item.isAdjacent(row, column, schematic)) {
                        totalCount += item.value
                    }
                }
            }
        }

        return totalCount
    }

    private fun readSchematic(input: List<String>): List<List<Item>> {
        val array = input.map { string: String ->
            val ignoreIndexes = mutableListOf<Int>()
            var currentRestValue = 0

            string.mapIndexed { index, c ->
                if (index in ignoreIndexes) {
                    val item = ItemIntRest(currentRestValue)
                    item
                } else if (c == '.') {
                    ItemNoOp
                } else if (c == '*') {
                    ItemGear
                } else if (c.isDigit()) {
                    val str = StringBuilder(c.toString())
                    for (i in index + 1 until string.length) {
                        if (string[i].isDigit()) {
                            str.append(string[i])
                            ignoreIndexes.add(i)
                        } else {
                            break
                        }
                    }
                    val value = str.toString().toInt()
                    currentRestValue = value
                    ItemIntStart(value)
                } else {
                    ItemSpecialChar
                }
            }
        }
        return array
    }

    private fun ItemIntStart.isAdjacent(row: Int, col: Int, array: List<List<Item>>): Boolean {
        val startCol = 0.coerceAtLeast(col - 1)
        val endCol = (array.first().size - 1).coerceAtMost(col + this.value.toString().length + 1)

        if (row -1 >= 0) {
            if (array[row-1].containsSpecialChars(startCol, endCol)) {
                return true
            }
        }

        if (array[row].containsSpecialChars(startCol, endCol)) {
            return true
        }

        if (row +1 < array.size) {
            if (array[row + 1].containsSpecialChars(startCol, endCol)) {
                return true
            }
        }

        return false
    }

    private fun List<Item>.containsSpecialChars(startCol: Int, endCol: Int): Boolean {
       return  subList(startCol, endCol).any { it is ItemSpecialChar }
    }
}

private sealed interface Item {

    data class ItemIntStart(val value: Int) : Item
    data class ItemIntRest(var value:Int) : Item
    data object ItemSpecialChar : Item
    data object ItemGear : Item
    data object ItemNoOp : Item
}
