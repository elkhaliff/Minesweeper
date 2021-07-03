fun main() {
    // write your code here
    val n = readLine()!!.toInt()
    val arr = Array(n) { readLine()!!.toInt() }
    val arrOut = IntArray(n) { 0 }
    var shift = readLine()!!.toInt()
    var a = 0
    if (shift > n) {
        shift = shift % n
    }
    for (i in n - shift until n) {
        arrOut[a++] = arr[i]
    }
    for (i in 0 until n - shift) {
        arrOut[a++] = arr[i]
    }
    println(arrOut.joinToString(" "))
}
