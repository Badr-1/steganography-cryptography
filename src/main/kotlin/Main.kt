import com.github.ajalt.mordant.rendering.TextColors.red

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        while (true) {
            print("Task (hide, show, exit): ")
            when (val cmd = readln()) {
                "hide" -> {
                    print("Input image 🖼 (relative path): ")
                    val inputImageName = readln()
                    print("Output image 🖼 (relative path): ")
                    val outputImageName = readln()
                    print("Secret ㊙: ")
                    val message = readln()
                    print("Password 🔑: ")
                    val password = readln()

                    if (!inject(inputImageName, message, password, outputImageName)) {
                        continue
                    }
                }

                "show" -> {
                    print("Input image 🖼: ")
                    val inputImageName = readln()
                    print("Password 🔑: ")
                    val password = readln()

                    if (!extract(inputImageName, password)) {
                        continue
                    }

                }

                "exit" -> {
                    println("Bye!")
                    break
                }

                else -> {
                    println(red("Wrong task: $cmd"))
                }
            }
        }
    }
}