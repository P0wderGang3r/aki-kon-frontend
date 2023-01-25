package errors

class ConnectionError(
    override val server: Boolean = false,
    override val type: Int = 0,
    override val text: String = "Ошибка при попытке соединения с сервером."
) : IError { }