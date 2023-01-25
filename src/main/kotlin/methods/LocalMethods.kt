package methods

import errors.CustomError
import errors.IError
import errors.NoError
import json.response.GetError
import jsonParser
import kotlinx.serialization.decodeFromString
import java.util.*

fun parseYesNo(question: String): Boolean {
    while (true) {
        print("$question ")
        val input = readln()

        if (input.isEmpty() ||
            input.lowercase(Locale.getDefault()) == "д"  ||
            input.lowercase(Locale.getDefault()) == "да" ||
            input.lowercase(Locale.getDefault()) == "y"  ||
            input.lowercase(Locale.getDefault()) == "yes"
        )
            return true
        if (input.lowercase(Locale.getDefault()) == "н"  ||
            input.lowercase(Locale.getDefault()) == "нет"||
            input.lowercase(Locale.getDefault()) == "n"  ||
            input.lowercase(Locale.getDefault()) == "no"
        )
            return false
    }
}

fun parseError(response: String): IError {
    //Делаем попытку прочитать ошибку
    try {
        val error: GetError = jsonParser.decodeFromString(response)
        return CustomError(
            type = Integer.parseInt(error.type),
            text = error.text
        )
    } catch (_: Exception) {}

    return NoError(text = "")
}