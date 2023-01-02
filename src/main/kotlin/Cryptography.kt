import kotlin.experimental.xor

fun encrypt(message: String, password: String): ByteArray {
    val passwordAsBytes = password.encodeToByteArray()
    val messageAsBytes = message.encodeToByteArray()
    val encryptedBytes = mutableListOf<Byte>()
    messageAsBytes.forEachIndexed { index, byte ->
        encryptedBytes.add(byte xor passwordAsBytes[index % passwordAsBytes.size])
    }

    encryptedBytes.add(0.toByte())
    encryptedBytes.add(0.toByte())
    encryptedBytes.add(3.toByte())

    return encryptedBytes.toByteArray()
}

fun decrypt(messageAsBytes: MutableList<Byte>, password: String): String {

    messageAsBytes.removeLast()
    messageAsBytes.removeLast()
    messageAsBytes.removeLast()

    val passwordAsBytes = password.encodeToByteArray()
    val decryptedBytes = mutableListOf<Byte>()
    messageAsBytes.forEachIndexed { index, byte ->
        decryptedBytes.add(byte xor passwordAsBytes[index % passwordAsBytes.size])
    }
    return decryptedBytes.toByteArray().toString(Charsets.UTF_8)

}