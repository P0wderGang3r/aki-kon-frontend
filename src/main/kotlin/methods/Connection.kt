package methods

import http.makeGetRequest
import requestURL

fun connectionChange(input: String) {
    requestURL = input
    println("Обнаружен ввод пользовательского URL: $requestURL.")
}

fun connectionCheckExtended(waitTime: Long): Boolean {
    println("Проверка соединения с сервером. Пожалуйста, подождите.")
    do {
        val response = makeGetRequest(requestPath = "/")
        if (response.type == 0) {
            println("Отсутствует соединение с сервером. Повторная попытка соединения через $waitTime мс.")
            Thread.sleep(waitTime)
        }
    } while (response.type == 0)

    println("Соединение с сервером: присутствует.")
    return true
}

fun connectionCheck(waitTime: Long): Boolean {
    val response = makeGetRequest(requestPath = "/")
    if (response.type == 0) {
        println("Отсутствует соединение с сервером. Повторная попытка соединения через $waitTime мс.")
        Thread.sleep(waitTime)
        return false
    }

    return true
}