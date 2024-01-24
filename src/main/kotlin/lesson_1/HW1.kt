package lesson_1

/**
 * Написать программу, которая обрабатывает введённые пользователем в консоль команды:
 * exit
 * help
 * add <Имя> phone <Номер телефона>
 * add <Имя> email <Адрес электронной почты>
 *
 * После выполнения команды, кроме команды exit, программа ждёт следующую команду.
 *
 * Имя – любое слово.
 * Если введена команда с номером телефона, нужно проверить, что указанный телефон может начинаться с +, затем идут только цифры.
 * При соответствии введённого номера этому условию – выводим его на экран вместе с именем, используя строковый шаблон.
 * В противном случае - выводим сообщение об ошибке.
 * Для команды с электронной почтой делаем то же самое, но с другим шаблоном – для простоты, адрес должен содержать три
 * последовательности букв, разделённых символами @ и точкой.
 *
 * Пример команд:
 * add Tom email tom@example.com
 * add Tom phone +7876743558
 */

// хранилище контактов
val contacts = HashMap<String, MutableMap<String, String>>()

/**
 * Для правильной разбивки и добавления в словарь contacts
 */
private fun addContact(name: String, contactType: String, value: String) {
    if (contactType == "phone") {
        if (Regex("""^\+\d+$""").matches(value)) { // строка начинается с + и ы строке одни цифры
            contacts[name] = mutableMapOf("phone" to value) // phone - ключ
            println("Added phone number for $name: $value")
        } else {
            println("Error: Invalid phone number format")
        }
    } else if (contactType == "email") {
        if (Regex("""^[a-zA-Z]+@[a-zA-Z]+\.[a-zA-Z]+$""").matches(value)) { //  строка по email
            contacts[name] = mutableMapOf("email" to value)
            println("Added email for $name: $value")
        } else {
            println("Error: Invalid email format")
        }
    }
}

fun main() {
    runProgram()
}

private fun runProgram() {
    while (true) {
        println("Введите команду ")
        when (val command = readlnOrNull()) { //switch
            "exit" -> break
            "help" -> println(
                "Доступные команды: " +
                        "exit, " +
                        "help, " +
                        "add <Имя> phone <Номер телефона>, " +
                        "add <Имя> email <Адрес электронной почты>"
            )

            else -> { // как default в java
                val parts = command?.split(" ") // разбиваем
                if (parts?.size == 4 && parts[2] == "phone") {
                    addContact(parts[1], "phone", parts[3])
                } else if (parts?.size == 4 && parts[2] == "email") {
                    addContact(parts[1], "email", parts[3])
                } else {
                    println("Error: Invalid command format")
                }
            }
        }
    }
}
