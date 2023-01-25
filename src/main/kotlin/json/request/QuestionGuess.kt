package json.request

fun jsonRequestQuestionGuess(sessionIdentifier: String, question: String, answer: Boolean, wish: String): String {
    return "{\"session\":\"$sessionIdentifier\", \"question\":\"$question\"," +
            "\"answer\":$answer, \"guess_answer\":true, \"guess\":\"$wish\"}"
}