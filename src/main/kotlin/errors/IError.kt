package errors

interface IError {
    val server: Boolean
    val type: Int
    val text: String
    fun printText() {
        println(text)
    }
}