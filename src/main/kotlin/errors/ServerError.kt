package errors

class ServerError(
    override val server: Boolean = true,
    override val type: Int,
    override val text: String
) : IError { }