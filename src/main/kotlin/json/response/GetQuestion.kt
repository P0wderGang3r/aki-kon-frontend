package json.response

import kotlinx.serialization.Serializable

@Serializable
class GetQuestion(val question: String) {
}