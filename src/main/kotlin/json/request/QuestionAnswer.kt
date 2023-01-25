package json.request

fun jsonRequestAnswer(sessionIdentifier: String, answer: Boolean): String {
    return "{\"session\":\"$sessionIdentifier\", \"answer\":$answer}"
}