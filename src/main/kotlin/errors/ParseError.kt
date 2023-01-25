package errors

class ParseError(
    override val server: Boolean = false,
    override val type: Int = 1,
    override val text: String = "Произошла ошибка при попытке сериализации Json."
) : IError { }