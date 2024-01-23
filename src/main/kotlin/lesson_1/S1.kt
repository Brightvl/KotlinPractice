package lesson_1

fun main() {
    taskOne(5, 6)
    taskTwo()
    task3(1, 2, 4)
}

/**
 * Написать простую программу, которая принимает в
 * качестве аргументов два целых числа и выводит на экран их
 * сумму. Всю программу можно написать внутри функции
 * main.
 *
 */
fun taskOne(x: Int, y: Int) = println(x + y)

/**
 * Реализовать функции sumAll, createOutputString и multiplyBy
 * так, чтобы программа выводила следующие строчки.
 * sumAll = 26
 * sumAll = 0
 * sumAll = 27
 * Alice has age of 42
 * Bob has age of 23
 * student Carol has age of 19
 * Daniel has age of 32
 * null
 * 12
 */
fun taskTwo() {
    println("sumAll = ${sumAll(1, 5, 20)}")
    println("sumAll = ${sumAll()}")
    println("sumAll = ${sumAll(2, 3, 4, 5, 6, 7)}")

    println(createOutputString("Alice"))
    println(createOutputString("Bob", 23))
    println(createOutputString(isStudent = true, name = "Carol", age = 19))
    println(createOutputString("Daniel", 32, isStudent = null))

    println(multiplyBy(null, 4))
    println(multiplyBy(3, 4))
}

/**
 * sumAll принимает переменное число аргументов типа Int. Возвращает сумму
 * всех чисел, либо 0, если не передан ни один аргумент.
 */
fun sumAll(vararg number: Int) = number.sum()

/**
 * createOutputString формирует строку, используя параметры name, age и
 * isStudent. У параметров age и isStudent есть значения по умолчанию.
 */
fun createOutputString(name: String, age: Int = 42, isStudent: Boolean? = false) =
    "${if (isStudent == true) "student " else ""}$name has age of $age"

/**
 * multiplyBy принимает два числа типа Int и возвращает их произведение.
 * Вместо первого числа, можно передать null, в этом случае функция должна
 * вернуть null.
 */
fun multiplyBy(num1: Int?, num2: Int) = if (num1 != null) num1 * num2 else null

/**
 * Написать программу, выводящую на экран фигуру из звёздочек.
 * a – количество звёздочек на первой строчке;
 * b – количество строк от первой до центральной и от
 * центральной до последней;
 * c – количество звёздочек, на которое увеличивается
 * последовательность с каждой строкой.
 */

fun task3(a: Int, b: Int, c: Int) {
    var d: Int = 0
    for (i in 1..b * 2 + 1) {
        if (i != 1) {
            if (i < b+2) {
                d += c
            } else {
                d -= c
            }
        }
        for (j in 1..a + d) {
            print("*")

        }
        println()
    }
}
