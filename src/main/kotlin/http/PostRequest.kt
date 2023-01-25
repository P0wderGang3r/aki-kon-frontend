package http

import errors.ConnectionError
import errors.IError
import errors.NoError
import http.templates.templatePost

fun makePostRequest(requestPath: String, requestJSON: String): IError {
    return try {
        NoError(
            text = templatePost(
                requestPath = requestPath,
                requestJSON = requestJSON
            )
        )

    } catch (e: Exception) {
        ConnectionError()
    }
}