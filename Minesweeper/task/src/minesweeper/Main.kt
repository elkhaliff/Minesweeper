package minesweeper

import kotlin.random.Random

class Miner(val cells: Int) {
    private val rows = cells // Количество строк
    private val cols = cells // Количество столбцов
    private var cntSetX = 0 // Общее количество установленных мин

    val miningCell = "X"        // заминированная ячейка
    val cloudCell = "."         // неисследованное облако
    val flagCell = "*"          // установленный флаг (возможно мина)
    val emptyCell = "/"         // исследованная - пустая ячейка
    val workCell = "#"          // рабочая ячейка для повторного исследования
//    val explosionCell = "!"     // взрыв в ячейке

    /**
     * Инициализация массива рабочей области (минного поля)
     */
    private val fieldMap: Array<Array<String>> = Array(rows, { Array(cols, {cloudCell}) })
    /**
     * Инициализация массива для мин (минного поля)
     */
    private val fieldMapMin: Array<Array<String>> = Array(rows, { Array(cols, {cloudCell}) })

    /**
     * Проверка содержимого ячейки поля на опеределенный тип
     */
    fun isTypeCell(row: Int, col: Int, type: String) = (fieldMap[row][col] == type)

    /**
     * Проверка на незаполненность
     */
    fun isEmpty(row: Int, col: Int) = (fieldMapMin[row][col] != miningCell)

    /**
     * Проверка на наличие цифры в клетке
     */
    fun isNumberCell(row: Int, col: Int) = (!isTypeCell(row, col, cloudCell) && !isTypeCell(row, col, flagCell)
                                            && !isTypeCell(row, col, workCell) && !isTypeCell(row, col, emptyCell))

    /**
     * Количество свободных ячеек (возможность установки мины)
     */
    fun isEmptyCells() = (rows*cols - cntSetX) != 0

    /**
     * Установка одной мины
     */
    fun setMine(row_: Int, col_: Int): Boolean {
        val row = row_ - 1
        val col = col_ - 1
        return if (isEmpty(row, col)) {
            fieldMapMin[row][col] = miningCell
            cntSetX++
            true
        } else false
    }

    /**
     *  Попытка найти место для одной мины
     */
    fun instalMine(){
        var doIt = true
        while (doIt && isEmptyCells()) {
            val r = Random.nextInt(1, rows+1)
            val c = Random.nextInt(1, cells+1)
            doIt = !setMine(r, c)
        }
    }

    /**
     *  Установка определенного кол-ва мин random
     */
    fun initMines(mines: Int) {
        for (i in 1..mines) {
            instalMine()
        }
    }

    /**
     *  Установка\снятие флага на предполагаемое место мины
     */
    fun setUnsetFlag(row: Int, col: Int) {
        fieldMap[row][col] = if (isTypeCell(row, col, flagCell)) cloudCell else flagCell
    }

    /**
     * Количество мин вокруг указанной ячейки (8 ячеек, исключая выход за пределы поля)
     */
    fun viewAround(row: Int, col: Int, markWork: Boolean = false) =
        checkRow((row - 1), (col - 1), markWork) +      // проверяем строку выше (все позиции)
        checkRow(row, (col - 1), markWork) +            // проверяем текущую строку
        checkRow((row + 1), (col - 1), markWork)        // проверяем строку ниже (все позиции)

    /**
     * Получение данных о трех ячейках строки (исключая выход за пределы поля)
     */
    fun checkRow(row: Int, col: Int, markWork: Boolean = false): Int {
        if (row < 0 || row > rows - 1 ) return 0 // такой ячейки - нет
        var out = 0
        for (c in col..col+2) {
            if (c < 0 || c > cells - 1) continue // такой ячейки - нет
            if (isEmpty(row, c)) {
                if (markWork && (isTypeCell(row, c, cloudCell) || (isTypeCell(row, c, flagCell)) && !isTypeCell(row, c, emptyCell)))
                    fieldMap[row][c] = workCell // покажем, что данная ячейка пуста, и вокруг можно исследовать
            } else out ++
        }
        return out
    }

    /**
     * Тестирование всего поля на отсутвие мины
     */
    fun testEmpty() {
        var isWork = true
        while (isWork) {
            isWork = false
            fieldMap.forEachIndexed { row, r ->
                r.forEachIndexed { col, c ->
                    if (isTypeCell(row, col, workCell)) {
                        testEmptyCell(row, col)
                        isWork = true
                    }
                }
            }
        }
    }

    /**
     * Тестирование ячейки на отсутвие мины
     */
    fun testEmptyCell(row: Int, col: Int): Boolean {
        val ie = isEmpty(row, col)
        if (!ie) {
//            fieldMapMin[row][col] = explosionCell
            return false
        }
        val around = viewAround(row, col)
        if (ie && (around != 0)) fieldMap[row][col] = around.toString()
        else if (around == 0) {
            viewAround(row, col, true)
            fieldMap[row][col] = emptyCell
        }
        return true
    }

    /**
     * Формирование строки из игрового поля данного класса
     * (в частности - получаем возможность вывода на печать)
     */
    override fun toString(): String {
        var n = 0
        var mapString = " |" + Array(cols, {++n}).joinToString("")+"|\n"
        val border = "—|" + Array(cols, {'—'}).joinToString("")+"|\n"
        mapString += border
        fieldMap.forEachIndexed { row, r ->
            mapString += "${row+1}|"
            r.forEachIndexed { col, c ->
                mapString += c
            }
            mapString += "|\n"
        }
        mapString += border
        return mapString
    }

    /**
     * Ход минера и проверка
     */
    fun setCoordinates(): Int {
        print("Set/unset mines marks or claim a cell as free: ")
        val input = readLine()!!.split(" ")

        // По заданию - ввод идет столбец - строка (x - y)
        val col = input[0].toInt() - 1
        val row = input[1].toInt() - 1
        val type = input[2]

        // There is a number here!
        if (isNumberCell(row, col)) return 1

        when (type) {
            "mine" -> setUnsetFlag(row, col)
            "free" -> if (testEmptyCell(row, col)) testEmpty() else return 2
        }

        return 0
    }

    /**
     * Проверка на завершение игры
     */
    fun checkFlags(): Int {
        fieldMapMin.forEachIndexed { row, r ->
            r.forEachIndexed { col, c ->
//                if (c == explosionCell) {
//                    fieldMapMin[row][col] = miningCell
//                    return 2
//                }
                if (!isEmpty(row, col) && !isTypeCell(row, col, flagCell)) return 1
            }
        }
        return 0
    }

    /**
     * Раскрытие всех мин
     */
    fun viewAllMine() {
        fieldMap.forEachIndexed { row, r ->
            r.forEachIndexed { col, c ->
                if (c == flagCell) {
                    fieldMap[row][col] = emptyCell
                }
            }
        }
        fieldMapMin.forEachIndexed { row, r ->
            r.forEachIndexed { col, c ->
                if (c == miningCell) {
                    fieldMap[row][col] = miningCell
                }
            }
        }
    }

}

fun getInt(str: String): Int {
    print(str)
    return readLine()!!.toInt()
}

fun main(args: Array<String>) {
    val cells = 9
    var out: Int
    var toIt = true
    var checkGame: Int
    val mines = getInt("How many mines do you want on the field? ")
    val mm = Miner(cells) // Инициализация экземпляра класса с заданной шириной поля
    mm.initMines(mines)
    println(mm)
    while (toIt) { // Цикл получения координат - ожидание хода, проверка результатов
        out = mm.setCoordinates() // Запрашиваем ход игрока, устанавливаем ход на доску
        when (out) {
            0 -> {
                checkGame = mm.checkFlags()
                println(mm)
                if (checkGame == 0) {
                    println("Congratulations! You found all the mines!")
                    return
                }
            }
            1 -> println("There is a number here!")
            2 ->  { mm.viewAllMine()
                println(mm)
                println("You stepped on a mine and failed!")
                return }
        }
    }
}