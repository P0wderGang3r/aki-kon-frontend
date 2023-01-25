package methods

import errors.CustomError
import errors.IError
import errors.NoError
import errors.ServerError
import http.makePostRequest
import json.request.jsonRequestSession
import json.request.jsonRequestUsername
import json.response.GetSession
import json.response.GetUsername
import jsonParser
import kotlinx.serialization.decodeFromString
import sessionIdentifier
import sessionUsername

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - CustomError() - ошибка выполнения
 * - NoError() - успешная авторизация
 */
fun userGet(): IError {
    println("Проверка введённого идентификатора пользовательской сессии.")

    //Выполняем запрос к серверу
    val response = makePostRequest(requestPath = "/user/get", requestJSON = jsonRequestSession(sessionIdentifier))
    if (response.type == 0)
        return response

    //Делаем попытку прочитать ошибку
    val error = parseError(response.text)
    if (error.type != -1)
        return ServerError(type = error.type, text = error.text)

    //Делаем попытку прочитать имя пользователя, соответствующее пользовательской сессии
    val result: GetUsername
    return try {
        result = jsonParser.decodeFromString(response.text)
        sessionUsername = result.username
        NoError(text = "Авторизация прошла успешно. Добро пожаловать, ${result.username}.")
    } catch (e: Exception) {
        CustomError(
            text = "Не удалось авторизоваться с предоставленным идентификатором пользовательской сессии."
        )
    }
}

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - CustomError(type = 2) - запрос на повторный ввод имени пользователя
 * - CustomError(type = 3) - отказ от повторного ввода имени пользователя
 * - NoError() - успешные регистрация и авторизация
 */
fun userNew(): IError {
    var username = ""
    //Допрашиваем пользователя на предмет непустого имени пользователя
    while (true) {
        print("Введите имя пользователя: ")
        username = readln()
        if (username.isNotBlank()) {
            break
        }
        println("Имя пользователя не может быть пустым.")
    }

    //Выполняем запрос к серверу
    val response = makePostRequest(requestPath = "/user/new", requestJSON = jsonRequestUsername(username))
    if (response.type == 0)
        return response

    //Делаем попытку прочитать ошибку
    val error = parseError(response.text)
    if (error.type != -1)
        return ServerError(type = error.type, text = error.text)

    //Создаём новую пользовательскую сессию
    val result: GetSession
    try {
        result = jsonParser.decodeFromString(response.text)
        sessionIdentifier = result.session
        sessionUsername = username
        return NoError(text = "Вы были успешно зарегистрированы и авторизованы с именем пользователя $username.")
    } catch (e: Exception) {
        //Если по каким-то причинам нам прилетел не идентификатор пользовательской сессии, повторяем ввод имени пользователя
        println("Не удалось создать новую пользовательскую сессию с предоставленным именем пользователя.")

        if (parseYesNo(question = "Повторить ввод имени пользователя? [ДА/нет]:"))
            return CustomError(type = 2, text = "")
        else
            return CustomError(type = 3,
                text = "Вы отказались от повторного ввода имени пользователя."
            )
    }
}