import kotlin.random.Random

fun generatePredictablePassword(seed: Int): String {
    var randomPassword = ""
    val randomGeneratorSeed = Random(seed) 
    for (i in 1..10)
        randomPassword += randomGeneratorSeed.nextInt(33, 127).toChar()
	return randomPassword
}
