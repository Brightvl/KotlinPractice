package lesson_2.HW2

/**
 * За основу берём код решения домашнего задания из предыдущего семинара и дорабатываем его.
 *
 * — Создайте иерархию sealed классов, которые представляют собой команды. В корне иерархии интерфейс Command.
 *
 * — В каждом классе иерархии должна быть функция isValid(): Boolean, которая возвращает true, если команда введена
 * с корректными аргументами.
 * Проверку телефона и email нужно перенести в эту функцию.
 *
 * — Напишите функцию readCommand(): Command, которая читает команду из текстового ввода, распознаёт её и возвращает
 * один из классов-наследников Command, соответствующий введённой команде.
 *
// * — Создайте data класс Person, который представляет собой запись о человеке. Этот класс должен содержать поля:
// * name – имя человека
// * phone – номер телефона
// * email – адрес электронной почты
 *
 * — Добавьте новую команду show, которая выводит последнее значение, введённой с помощью команды add. Для этого
 * значение должно быть сохранено в переменную типа Person. Если на момент выполнения команды show не было ничего
 * введено, нужно вывести на экран сообщение “Not initialized”.
 *
 * — Функция main должна выглядеть следующем образом. Для каждой команды от пользователя:
 * Читаем команду с помощью функции readCommand
 * Выводим на экран получившийся экземпляр Command
 * Если isValid для команды возвращает false, выводим help. Если true, обрабатываем команду внутри when.
 */


//region sealed
sealed interface Command {
    fun isValid(): Boolean
}

data class AddPhone(val name: String, val phone: String) : Command {
    override fun isValid() = Regex("""^\+\d+$""").matches(phone)
}

data class AddEmail(val name: String, val email: String) : Command {
    override fun isValid() = Regex("""^[a-zA-Z]+@[a-zA-Z]+\.[a-zA-Z]+$""").matches(email)
}

object Help : Command {
    override fun isValid() = true
}

object Exit : Command {
    override fun isValid() = true
}
//endregion

data class Person(var name: String, var phone: String? = null, var email: String? = null)

fun main() {
    val contacts = mutableMapOf<String, Person>()
    var lastPerson: Person? = null

    while (true) {
        try {
            val command = readCommand()
            if (!command.isValid()) {
                showCommands()
                continue
            }

            when (command) {
                is Exit -> break
                is Help -> showCommands()
                is AddPhone -> {
                    val person = contacts.getOrPut(command.name) { Person(command.name) }
                    person.phone = command.phone
                    lastPerson = person
                    println("Added phone number for ${person.name}: ${person.phone}")
                }

                is AddEmail -> {
                    val person = contacts.getOrPut(command.name) { Person(command.name) }
                    person.email = command.email
                    lastPerson = person
                    println("Added email for ${person.name}: ${person.email}")
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Ошибка: Неверная команда")
        }
    }

    lastPerson?.let { println("Last added person: $it") } ?: println("Not initialized")
}


fun readCommand(): Command {
    val input = readLine()!!.split(" ")
    return when (input[0]) {
        "exit" -> Exit
        "help" -> Help
        "add" -> {
            val name = input[1]
            when (input[2]) {
                "phone" -> AddPhone(name, input[3])
                "email" -> AddEmail(name, input[3])
                else -> throw IllegalArgumentException("Invalid command")
            }
        }

        else -> throw IllegalArgumentException("Invalid command")
    }
}

fun showCommands() {
    println(
        "Доступные команды: " +
                "exit, " +
                "help, " +
                "add <Имя> phone <Номер телефона>, " +
                "add <Имя> email <Адрес электронной почты>"
    )
}
