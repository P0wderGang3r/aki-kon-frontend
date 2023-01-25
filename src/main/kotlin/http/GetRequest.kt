package http

import errors.ConnectionError
import errors.IError
import errors.NoError
import http.templates.templateGet


fun makeGetRequest(requestPath: String): IError {
    return try {
        NoError(
            text = templateGet(
                requestPath = requestPath
            )
        )
    } catch (e: Exception) {
        ConnectionError()
    }
}