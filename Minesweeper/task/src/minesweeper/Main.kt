package minesweeper

class Miner(val cells: Int) {
    private val rows = cells // Количество строк
    private val cols = cells // Количество столбцов
    private var cntX = 0 // Общее количество ходов

    val miningCell = "X"
    val emptyCel = "."

    /**
     * Инициализация массива рабочей области (минного поля)
     */
    private val fieldMap: Array<Array<String>> = Array(rows, { Array(cols, {emptyCel}) })

    /**
     * Ход тральщика
     */
    fun makeMine(row: Int, col: Int) {
        fieldMap[row-1][col-1] = miningCell
    }


    /**
     * Проверка на незаполненность
     */
    fun isEmpty(row: Int, col: Int) = (fieldMap[row-1][col-1] == emptyCel)

    /**
     * Получение столбца данных
     */
    fun getCol(col: Int): String {
        var out = ""
        for (r in 1..rows) {
            out += fieldMap[r-1][col-1]
        }
        return out
    }

    /**
     * Получение строки данных
     */
    fun getRow(row: Int): String {
        var out = ""
        for (c in 1..cols) {
            out += fieldMap[row-1][c-1]
        }
        return out
    }

    /**
     * Получение правой диагонали
     */
    fun getRightDiagonal(): String {
        var out = ""
        for (c in 1..cells) {
            out += fieldMap[c-1][c-1]
        }
        return out
    }

    /**
     * Получение левой диагонали
     */
    fun getLeftDiagonal(): String {
        var out = ""
        var c = cells
        for (r in 1..cells) {
            out += fieldMap[r-1][--c]
        }
        return out
    }

    /**
     *  Заполнение переменной с массивом игровой доски - строкой с данными
     */
    fun initMines(mines: Int) {
        val str = "4816935824" // put random mines
        for (r in 1..rows)
            for (c in 1..cols)
                if (c == str[c].toInt())
                    fieldMap[r-1][c-1] = miningCell
    }

    /**
     * Формирование строки из игрового поля данного класса
     * (в частности - получаем возможность вывода на печать)
     */
    override fun toString(): String {
        val border = Array(cols*3, {'-'}).joinToString("")+"\n"
        var mapString = border

        fieldMap.forEach { row ->
            mapString += "| "
            row.forEach { c ->
                mapString += "$c "
            }
            mapString += "|\n"
        }

        mapString += border

        return mapString
    }

    /**
     * Статистика крестиков и ноликов
     */
    fun statField(){
        cntX = 0
        fieldMap.forEach { row ->
            row.forEach { c ->
                when (c) {
                    "X" -> cntX++
                }
            }
        }
    }

    /**
     * Количество свободных ячеек (возможность хода)
     */
    fun isEmptyCell() = (rows*cols - cntX)>0

    /**
     * Ход минера и проверка
     */
    fun stepMiners(step: Char): Int {
        print("Enter the coordinates (${step}): ")
        val cells = readLine()!!.split(' ')

        val isNumeric = (cells[0].matches("\\d+".toRegex())) &&
                (cells[1].matches("\\d+".toRegex()))
        if (!isNumeric) {
            return 1    // You should enter numbers!
        }

        val row = cells[0].toInt()
        val col = cells[1].toInt()

        if (!(row in 1..rows) || !(col in 1..cols)) {
            return 2    // Coordinates should be from 1 to side!
        }

        if (!isEmpty(row, col)) {
            return 3    // This cell is occupied! Choose another one!
        }

        if (step == 'X') {
            makeMine(row, col)
       }

        return 0
    }

}

fun main(args: Array<String>) {
    val cells = 9
    val mines = 10
    val mm = Miner(cells) // Инициализация экземпляра класса с заданной шириной поля
    mm.initMines(mines)
    println(mm)
}
