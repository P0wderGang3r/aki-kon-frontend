package json.request

fun jsonRequestSession(sessionIdentifier: String): String {
    return "{\"session\":\"$sessionIdentifier\"}"
}