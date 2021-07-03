fun main() {
    // write your code here
    val n = readLine()!!.toInt()
    var cnt = 0
    val arrN = Array<Int>(n) { readLine()!!.toInt() }
    for (i in 0 until n - 2) {
        if (arrN[i] + 1 == arrN[i + 1] &&
            arrN[i + 1] + 1 == arrN[i + 2]) {
            cnt++
        }
    }
    println(cnt)
}
