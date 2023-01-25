package errors

class CustomError(
    override val server: Boolean = false,
    override val type: Int = 2,
    override val text: String
) : IError { }