package json.response

import kotlinx.serialization.Serializable

@Serializable
class GetError(val type: String, val text: String) {
}