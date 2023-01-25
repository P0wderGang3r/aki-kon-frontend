package methods

import errors.IError
import errors.NoError
import errors.ParseError
import errors.ServerError
import http.makePostRequest
import json.request.jsonRequestQuestionGuess
import json.request.jsonRequestSession
import json.response.GetStatus
import jsonParser
import kotlinx.serialization.decodeFromString
import sessionIdentifier

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - NoError() - конец игровой сессии
 */
fun guessIsCorrect(input: String): IError {

    val rawAnswer = input.split("|")[0]
    var questionAnswer = true
    if (rawAnswer == "true") {
        questionAnswer = true
    }
    if (rawAnswer == "false") {
        questionAnswer = false
    }

    val guess = input.split("|")[1]

    println("Загаданное вами животное - это ${guess}?")
    val answer = parseYesNo(question = "Ваш ответ:")

    //Если пользователь согласен с тем, что предложенное животное является загаданным, то отправляем запрос согласия
    if (answer) {
        return isCorrect()
    }

    //Если пользователь не согласен, то создаём новый вопрос
    return isNotCorrect(questionAnswer, guess)
}

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - NoError() - конец игровой сессии
 */
fun isCorrect(): IError {
    //Выполняем запрос к серверу
    val response = makePostRequest("/guess/correct", jsonRequestSession(sessionIdentifier))
    if (response.type == 0)
        return response

    //Делаем попытку прочитать ошибку
    val error = parseError(response.text)
    if (error.type != -1)
        return ServerError(type = error.type, text = error.text)

    //Делаем попытку прочитать ответ от сервера
    return try {
        val result: GetStatus = jsonParser.decodeFromString(response.text)

        NoError(text = "Достигнут конец игровой сессии.\n")
    } catch (e: Exception) {
        ParseError()
    }
}

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - NoError() - конец игровой сессии
 */
fun isNotCorrect(answer: Boolean, guess: String): IError {
    //Спрашиваем загаданное животное
    println("Какое животное вы загадали?")
    var wish = ""
    while (wish.isBlank()) {
        wish = readln()
    }

    //Спрашиваем, чем это животное отличается от предположенного
    println("Чем животное $wish отличается от животного $guess?")
    var diff = ""
    while (diff.isBlank()) {
        diff = readln()
    }

    //Выполняем запрос к серверу
    val response =
        makePostRequest(requestPath = "/question/add", jsonRequestQuestionGuess(sessionIdentifier, diff, answer, wish))
    if (response.type == 0)
        return response

    //Делаем попытку прочитать ошибку
    val error = parseError(response.text)
    if (error.type != -1)
        return ServerError(type = error.type, text = error.text)

    //Делаем попытку прочитать ответ от сервера
    return try {
        val result: GetStatus = jsonParser.decodeFromString(response.text)

        NoError(text = "Достигнут конец игровой сессии.\n")
    } catch (e: Exception) {
        ParseError()
    }
}