package lesson_4.hw4

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess


/**
 * — Добавьте новую команду export, которая экспортирует добавленные значения в текстовый файл в формате JSON. Команда принимает путь к новому файлу. Например
 * export /Users/user/myfile.json
 * — Реализуйте DSL (предметно ориентированный язык) на Kotlin, который позволит конструировать JSON и преобразовывать его в строку.
 * — Используйте этот DSL для экспорта данных в файл.
 * — Выходной JSON не обязательно должен быть отформатирован, поля объектов могут идти в любом порядке. Главное, чтобы он имел корректный синтаксис. Такой вывод тоже принимается:
 * [{"emails": ["ew@huh.ru"],"name": "Alex","phones": ["34355","847564"]},{"emails": [],"name": "Tom","phones": ["84755"]}]
 *
 * Записать текст в файл можно при помощи удобной функции-расширения writeText:
 * File("/Users/user/file.txt").writeText("Text to write")
 *
 * Под капотом она использует такую конструкцию
 *
 *
 * FileOutputStream(file).use {
 * it.write(text.toByteArray(Charsets.UTF_8))
 * }
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
            it.value.phones.contains(value) || it.value.emails.contains(value)
        }
        if (matchingContacts.isNotEmpty()) {
            println("Найденные контакты:")
            for ((name, person) in matchingContacts) {
                println(
                    "$name - Phones: ${
                        person.phones.joinToString(", ")
                    } - Emails: " +
                            person.emails.joinToString(", ")
                )
            }
        } else {
            println("Контакты не найдены.")
        }
    }
}

data class Export(val path: String) : Command {
    override fun isValid() = true

    override fun execute() {
        val json = json {
            contacts.forEach { (name, person) ->
                jsonObject(name) {
                    jsonArray("phones") {
                        person.phones.forEach { jsonString(it) }
                    }
                    jsonArray("emails") {
                        person.emails.forEach { jsonString(it) }
                    }
                }
            }
        }
        File(path).writeText(json.toJson())
    }

    sealed class JsonElement {
        abstract fun toJson(): String
    }

    data class JsonObject(val entries: MutableMap<String, JsonElement> = mutableMapOf()) : JsonElement() {
        override fun toJson() =
            entries.entries.joinToString(prefix = "{", postfix = "}") { "\"${it.key}\": ${it.value.toJson()}" }
    }

    data class JsonArray(val items: MutableList<JsonElement> = mutableListOf()) : JsonElement() {
        override fun toJson() = items.joinToString(prefix = "[", postfix = "]") { it.toJson() }
    }

    data class JsonString(val value: String) : JsonElement() {
        override fun toJson() = "\"$value\""
    }

    fun json(init: JsonObject.() -> Unit): JsonObject = JsonObject().apply(init)

    fun JsonObject.jsonObject(key: String, init: JsonObject.() -> Unit) {
        entries[key] = JsonObject().apply(init)
    }

    fun JsonObject.jsonArray(key: String, init: JsonArray.() -> Unit) {
        entries[key] = JsonArray().apply(init)
    }


    fun JsonArray.jsonString(value: String) {
        items.add(JsonString(value))
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
        "export" -> Export(input[1])
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
