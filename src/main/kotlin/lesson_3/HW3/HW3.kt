package lesson_3.HW3

import kotlin.system.exitProcess


/**
 * — Измените класс Person так, чтобы он содержал список телефонов и список почтовых адресов, связанных с человеком.
 * — Теперь в телефонной книге могут храниться записи о нескольких людях. Используйте для этого наиболее подходящую структуру данных.
 * — Команда AddPhone теперь должна добавлять новый телефон к записи соответствующего человека.
 * — Команда AddEmail теперь должна добавлять новый email к записи соответствующего человека.
 * — Команда show должна принимать в качестве аргумента имя человека и выводить связанные с ним телефоны и адреса электронной почты.
 * — Добавьте команду find, которая принимает email или телефон и выводит список людей, для которых записано такое значение.
 */

sealed interface Command {
    fun isValid(): Boolean
    fun execute()
}

data class AddPhone(val name: String, val phone: String) : Command {
    override fun isValid() = Regex("""^\+\d+$""").matches(phone)

    override fun execute() {
        val person = contacts.getOrPut(name) { Person(name) }
        person.phones.add(phone)
        println("Контакт $person добавлен")
    }
}

data class AddEmail(val name: String, val email: String) : Command {
    override fun isValid() = Regex("""^[a-zA-Zа-яА-Я0-9]+@[a-zA-Z]+\.[a-zA-Z]+$""").matches(email)

    override fun execute() {
        val person = contacts.getOrPut(name) { Person(name) }
        person.emails.add(email)
        println("Контакт $person добавлен")
    }
}

data object Help : Command {
    override fun isValid() = true

    override fun execute() {
        val commands =
            listOf(
                "exit",
                "help",
                "add <Имя> phone <Номер телефона>",
                "add <Имя> email <Адрес электронной почты>",
                "show <Имя>",
                "find <Телефон или Email>"
            )
        println("Доступные команды: ${commands.joinToString(", ")}")
    }
}

data object Exit : Command {
    override fun isValid() = true

    override fun execute() {
        exitProcess(0)
    }
}

data class Show(val name: String?) : Command {
    override fun isValid() = true

    override fun execute() {
        val person = contacts[name]
        if (person != null) {
            println("${person.name}'s phones: ${person.phones.joinToString(", ")}")
            println("${person.name}'s emails: ${person.emails.joinToString(", ")}")
        } else {
            println("Контакт не найден.")
        }
    }
}

data class Find(val value: String?) : Command {
    override fun isValid() = true

    override fun execute() {
        val matchingContacts = contacts.filter {
            it.value.phones.contains(value) || it.value.emails.contains(value) }
        if (matchingContacts.isNotEmpty()) {
            println("Найденные контакты:")
            for ((name, person) in matchingContacts) {
                println("$name - Phones: ${
                    person.phones.joinToString(", ")} - Emails: " +
                        person.emails.joinToString(", ")
                )
            }
        } else {
            println("Контакты не найдены.")
        }
    }
}

data class Person(
    var name: String,
    var phones: MutableList<String> = mutableListOf(),
    var emails: MutableList<String> = mutableListOf()
)

val contacts = mutableMapOf<String, Person>()

fun readCommand(): Command {
    print("Введите команду: ")
    val input = readLine()?.split(" ") ?: throw IllegalArgumentException("Неверная команда")
    return when (input[0]) {
        "exit" -> Exit
        "help" -> Help
        "add" -> {
            val name = input[1]
            when (input[2]) {
                "phone" -> AddPhone(name, phone = input[3])
                "email" -> AddEmail(name, email = input[3])
                else -> throw IllegalArgumentException("Неверная команда")
            }
        }

        "show" -> Show(input.getOrNull(1))
        "find" -> Find(input.getOrNull(1))
        else -> throw IllegalArgumentException("Неверная команда")
    }
}

fun main() {
    while (true) {
        try {
            val command = readCommand()
            if (command.isValid()) {
                command.execute()
            }
        } catch (e: IllegalArgumentException) {
            println("Ошибка: ${e.message}")
        }
    }
}

