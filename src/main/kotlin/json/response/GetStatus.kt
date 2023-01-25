package json.response

import kotlinx.serialization.Serializable

@Serializable
class GetStatus(val status: String) {
}