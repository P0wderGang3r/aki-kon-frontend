package http.templates

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import requestURL

private val client = OkHttpClient()

val JSON = "application/json".toMediaType()

fun templatePost(requestPath: String, requestJSON: String): String {
    val body: RequestBody = requestJSON.toRequestBody(JSON)

    val request = Request.Builder()
        .url(url = requestURL + requestPath)
        .post(body = body)
        .build()

    client.newCall(request).execute().use { response ->
        return response.body!!.string()
    }
}