package lesson_3.HW3

/**
 * — Измените класс Person так, чтобы он содержал список телефонов и список почтовых адресов, связанных с человеком.
 * — Теперь в телефонной книге могут храниться записи о нескольких людях. Используйте для этого наиболее подходящую структуру данных.
 * — Команда AddPhone теперь должна добавлять новый телефон к записи соответствующего человека.
 * — Команда AddEmail теперь должна добавлять новый email к записи соответствующего человека.
 * — Команда show должна принимать в качестве аргумента имя человека и выводить связанные с ним телефоны и адреса электронной почты.
 * — Добавьте команду find, которая принимает email или телефон и выводит список людей, для которых записано такое значение.
 */
//region sealed class
sealed class Command {
    abstract fun isValid(): Boolean
}

data class AddPhone(val name: String, val phone: String) : Command() {
    override fun isValid() = Regex("""^\+\d+$""").matches(phone)
}

data class AddEmail(val name: String, val email: String) : Command() {
    override fun isValid() = Regex("""^[a-zA-Z]+@[a-zA-Z]+\.[a-zA-Z]+$""").matches(email)
}

data class Show(val name: String) : Command() {
    override fun isValid() = true
}

data class Find(val contact: String) : Command() {
    override fun isValid() = true
}

data object Help : Command() {
    override fun isValid() = true
}

data object Exit : Command() {
    override fun isValid() = true
}
//endregion

data class Person(var name: String, var phones: MutableList<String> = mutableListOf(), var emails: MutableList<String> = mutableListOf())

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
                    person.phones.add(command.phone)
                    lastPerson = person
                    println("Добавлен телефон для ${person.name}: Телефоны=${person.phones.joinToString(", ")}")
                }
                is AddEmail -> {
                    val person = contacts.getOrPut(command.name) { Person(command.name) }
                    person.emails.add(command.email)
                    lastPerson = person
                    println("Добавлен email для ${person.name}: Emails=${person.emails.joinToString(", ")}")
                }
                is Show -> {
                    val person = contacts[command.name]
                    if (person != null) {
                        println("Контакт для ${person.name}: Телефоны=${person.phones.joinToString(", ")}, Emails=${person.emails.joinToString(", ")}")
                    } else {
                        println("Контакт не найден")
                    }
                }
                is Find -> {
                    val foundPersons = contacts.values.filter { it.phones.contains(command.contact) || it.emails.contains(command.contact) }
                    if (foundPersons.isNotEmpty()) {
                        println("Найденные контакты:")
                        foundPersons.forEach { person ->
                            println("${person.name}: Телефоны=${person.phones.joinToString(", ")}, Emails=${person.emails.joinToString(", ")}")
                        }
                    } else {
                        println("Контакт не найден")
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Ошибка: ${e.message}")
        }
    }

    lastPerson?.let { println("Последний добавленный человек: $it") } ?: println("Не инициализирован")
}

fun readCommand(): Command {
    val input = readln().split(" ")
    return when (input[0]) {
        "exit" -> Exit
        "help" -> Help
        "add" -> {
            val name = input[1]
            when (input[2]) {
                "phone" -> AddPhone(name, input[3])
                "email" -> AddEmail(name, input[3])
                else -> throw IllegalArgumentException("Неверная команда")
            }
        }
        "show" -> Show(input[1])
        "find" -> Find(input[1])
        else -> throw IllegalArgumentException("Неверная команда")
    }
}

fun showCommands() {
    val commands = listOf("exit", "help", "add <Имя> phone <Номер телефона>", "add <Имя> email <Адрес электронной почты>", "show <Имя>", "find <Телефон или Email>")
    println("Доступные команды: ${commands.joinToString(", ")}")
}
