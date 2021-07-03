fun main() {
    var max = readLine()!!.toInt()
    val arr = Array(max) { readLine()!!.toInt() }
    max = arr.maxOrNull()!!
    println(arr.indexOf(max))
}