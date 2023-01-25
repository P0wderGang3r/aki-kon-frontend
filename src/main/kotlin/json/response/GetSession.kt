package json.response

import kotlinx.serialization.Serializable

@Serializable
class GetSession(val session: String) {
}