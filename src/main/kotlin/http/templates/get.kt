package http.templates

import okhttp3.OkHttpClient
import okhttp3.Request
import requestURL

private val client = OkHttpClient()

fun templateGet(requestPath: String): String {

    val request = Request.Builder()
        .url(url = requestURL + requestPath)
        .get()
        .build()

    client.newCall(request).execute().use { response ->
        if (response.isSuccessful) {
            return response.body!!.string()
        }
    }

    return ""
}