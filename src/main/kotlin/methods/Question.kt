package methods

import errors.IError
import errors.NoError
import errors.ParseError
import errors.ServerError
import http.makePostRequest
import json.request.jsonRequestAnswer
import json.request.jsonRequestSession
import json.response.GetGuess
import json.response.GetQuestion
import jsonParser
import kotlinx.serialization.decodeFromString
import sessionIdentifier

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - NoError() - следующий вопрос
 */
fun questionAsk(): IError {

    //Выполняем запрос к серверу
    val response = makePostRequest(requestPath = "/question/get", requestJSON = jsonRequestSession(sessionIdentifier))
    if (response.type == 0)
        return response

    //Делаем попытку прочитать ошибку
    val error = parseError(response.text)
    if (error.type != -1)
        return ServerError(type = error.type, text = error.text)

    //Делаем попытку прочитать вопрос
    val result: GetQuestion
    return try {
        result = jsonParser.decodeFromString(response.text)
        NoError(text = "Это животное ${result.question}?")
    } catch (e: Exception) {
        ParseError()
    }
}

/**
 * Выходные данные:
 * - ConnectionError()
 * - ParseError()
 * - ServerError()
 * - NoError(type = -2) - предположение
 * - NoError(type = -3) - переход к следующему вопросу
 */
fun questionAnswer(): IError {
    val answer = parseYesNo(question = "Ваш ответ:")

    //Выполняем запрос к серверу
    val response =
        makePostRequest(requestPath = "/question/answer", requestJSON = jsonRequestAnswer(sessionIdentifier, answer))
    if (response.type == 0)
        return response

    //Делаем попытку прочитать ошибку
    val error = parseError(response.text)
    if (error.type != -1)
        return ServerError(type = error.type, text = error.text)

    //Делаем попытку прочитать догадку (или информацию о переходе к следующему вопросу)
    val result: GetGuess
    try {
        result = jsonParser.decodeFromString(response.text)

        //Финальная догадка
        if (Integer.parseInt(result.answer_type) == 0) {
            return NoError(type = -2, text = "${answer}|${result.guess}")
        }

        //Остались вопросы
        return NoError(type = -3, text = "Переход к следующему вопросу.")
    } catch (e: Exception) {
        return ParseError()
    }
}