package json.response

import kotlinx.serialization.Serializable

@Serializable
class GetGuess(val answer_type: String, val guess: String) {
}