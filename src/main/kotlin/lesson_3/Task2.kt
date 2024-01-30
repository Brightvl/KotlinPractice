package lesson_3

/*
Написать функцию-расширение calc к классу List, такую что
- Она имеет обобщенный параметр, такой же как тип элемента списка.
- Первым аргументом она принимает s типа Int.
- Вторым аргументом – лямбда-выражение, принимающее тип элемента массива и возвращающее Int.
- Функция должна применять лямбда-выражение к каждому s элементу списка и суммировать значения,
  полученные при вычислении лямбда-выражения. Итоговая сумма будет результатом функции calc.
 */
fun <T> List<T>.calc(s: Int, param: (T) -> Int): Int {
    return this.map { param(it) }.filterIndexed { arg1, _ -> arg1 % s == 0 }.sum()
}

fun main() {
    val list1 = listOf("aaa", "add", "ajhw", "yewt", "yedv")
    println(list1.calc(2) {
        it.length
    })

    val list2 = listOf(3, 6, 4, 8, 11, 32, 2, 6, 21, 3)
    println(list2.calc(3) {
        it
    })

    val list3 = emptyList<Double>()
    println(list3.calc(1) {
        it.toInt()
    })
}