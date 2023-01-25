package errors

class NoError(
    override val server: Boolean = false,
    override val type: Int = -1,
    override val text: String
) : IError { }