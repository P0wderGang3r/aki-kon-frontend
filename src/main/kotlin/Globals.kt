import kotlinx.serialization.json.Json

var requestURL: String = "http://0.0.0.0:8080"

var sessionIdentifier: String = ""

var sessionUsername: String = ""

val jsonParser = Json {
    ignoreUnknownKeys = true
}