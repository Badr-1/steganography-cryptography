import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

fun inject(inputImageName: String, message: String, password: String, outputImageName: String): Boolean {
    val inputImageFile = File(inputImageName)
    if (!File(inputImageName).exists()) {
        println("Can't read input file!")
        return false
    }
    val encryptedBytes = encrypt(message, password)
    val inputImage = ImageIO.read(inputImageFile)
    if (inputImage.width * inputImage.height < encryptedBytes.size * 8) {
        println("The input image is not large enough to hold this message.")
        return false
    }
    var bitsAdded = ""
    var index = 0
    var bits = 7

    for (y in 0 until inputImage.height) {
        for (x in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(x, y))
            var bit = color.blue.toByte().getBit(0)
            if (index <= encryptedBytes.lastIndex) {
                bit = encryptedBytes[index].getBit(bits)
                bitsAdded += bit.toString()
                bits--
            }
            val blue = color.blue shr 1 shl 1 or bit
            val newColor = Color(color.red, color.green, blue)
            inputImage.setRGB(x, y, newColor.rgb)
            if (bits == -1) {
                bits = 7
                bitsAdded = ""
                index++
            }
        }
    }
    val outputImageFile = File(outputImageName)
    ImageIO.write(inputImage, "png", outputImageFile)
    println("Message saved in $outputImageName image.")

    return true
}

fun extract(inputImageName: String, password: String): Boolean {

    if (!File(inputImageName).exists()) {
        println("Can't read input file!")
        return false
    }
    val messageAsBytes = mutableListOf<Byte>()
    val bits = mutableListOf<Int>()
    val inputImageFile = File(inputImageName)
    val inputImage = ImageIO.read(inputImageFile)
    extractMessage@ for (y in 0 until inputImage.height) {
        for (x in 0 until inputImage.width) {
            val color = Color(inputImage.getRGB(x, y), true)
            bits.add(color.blue and 1)
            if (bits.size == 8) {
                bits.reverse()
                var value = 0
                bits.forEachIndexed { index, i ->
                    value += i shl index
                }
                messageAsBytes.add(value.toByte())
                bits.clear()
            }

            if (messageAsBytes.size > 3 && messageAsBytes.last()
                    .toInt() == 3 && messageAsBytes[messageAsBytes.lastIndex - 1].toInt() == 0 && messageAsBytes[messageAsBytes.lastIndex - 2].toInt() == 0
            ) {
                break@extractMessage
            }
        }
    }
    println(decrypt(messageAsBytes, password))
    return true
}

fun Byte.getBit(position: Int): Int {
    return this.toInt() shr position and 1
}