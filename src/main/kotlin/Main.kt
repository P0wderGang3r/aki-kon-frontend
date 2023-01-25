import methods.*

fun helpText() {
    println(
        "-u --url <url: строка>                      | ввод собственного адреса сервера игры,\n" +
        "-s --session <идентификатор сессии: строка> | ввод существующего идентификатора сессии,\n" +
        "-h --help <без аргументов>                  | отображение данного сообщения."
    )
}

fun main(args: Array<String>) {

    var isGoingToContinue = true

    /*
    for (arg in args) {
        println(arg)
    }
     */

    //Проверка на запрос о помощи
    for (arg in args) {
        if (arg == "--help" || arg == "-h") {
            helpText()
            return
        }
    }

    //Проверка на пользовательский ввод URL
    for (index in args.indices) {
        if (
            (args[index] == "--url" || args[index] == "-u") &&
            (index + 1 != args.size)
        ) {
            connectionChange(args[index + 1])
            println()
            break
        }
    }

    //Проверяем соединение
    connectionCheckExtended(2500)
    println()

    if (args.isEmpty()) {
        userNew().printText()
    }
    else {
        for (index in args.indices) {
            if (
                (args[index] == "--session" || args[index] == "-s") &&
                (index + 1 != args.size)
            ) {
                sessionIdentifier = args[index + 1]
                break
            }
        }
        val checkResult = userGet()
        if (checkResult.type != -1) {
            //До потери пульса пытаемся создать новую пользовательскую сессию
            while (isGoingToContinue) {
                //Проверяем соединение с интернетом
                if (!connectionCheck(2500)) {
                    continue
                }

                //Пытаемся создать новую пользовательскую сессию
                val newSession = userNew()
                //Если создали, то заканчиваем цикл
                if (newSession.type == -1) {
                    newSession.printText()
                    break
                }
                //Если пользователь отказался вводить повторно имя пользователя, то заканчиваем игру
                if (newSession.type == 3) {
                    isGoingToContinue = false
                    newSession.printText()
                }
            }
        } else
            checkResult.printText()
    }

    if (!isGoingToContinue)
        println("Завершение работы программного средства.")

    println()
    println("Начало игровой сессии.")

    //Бесконечный цикл
    while (isGoingToContinue) {
        //Проверяем соединение
        if (!connectionCheck(2500)) {
            continue
        }

        //Получаем вопрос от сервера. Если ошибка соединения, заходим повторно
        val ask = questionAsk()
        ask.printText()
        //Если проблема с соединением, то в начало цикла
        if (!ask.server && ask.type == 0) {
            continue
        }
        //Если ошибка с поиском сессии, то завершаем игру
        if (ask.server && ask.type == 2) {
            isGoingToContinue = false
            break
        }

        //Даём ответ на вопрос
        val answer = questionAnswer()
        //Если вернулась не догадка, то выводим результат работы функции
        if (answer.type != -2)
            answer.printText()

        //Если проблема с соединением, то в начало цикла
        if (!answer.server && answer.type == 0) {
            continue
        }
        //Если ошибка с поиском сессии, то завершаем игру
        if (answer.server && answer.type == 2) {
            isGoingToContinue = false
            break
        }

        //Пока можем отвечать на вопросы, не входим внутрь. Если вопросы кончились, то входим внутрь
        if (!answer.server && answer.type == -2) {
            //Проверяем предположение
            val isCorrect = guessIsCorrect(answer.text)
            isCorrect.printText()
            //Если ошибка с поиском сессии, то завершаем игру
            if (answer.server && isCorrect.type == 2) {
                isGoingToContinue = false
                break
            }

            isGoingToContinue = parseYesNo("Желаете ли вы продолжить игру?")
        }
    }

    println("До свидания, $sessionUsername.")
}