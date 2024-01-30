package lesson_2.S2

fun printOperationResult(operation: Operation) {
    val result = operation.calculate()
    println(result)
}
sealed interface Operation {
    fun calculate(): Double
}

fun main() {
    // Соответствует формуле 4 + 2.5 * 2
    printOperationResult(
        Plus(
            Value(4.0),
            Multiply(
                Value(2.5),
                Value(2.0)
            )
        )
    )

    // Соответствует формуле (1 + 3.5) * (2.5 + 2)
    printOperationResult(
        Multiply(
            Plus(
                Value(1.0),
                Value(3.5)
            ),
            Plus(
                Value(2.5),
                Value(2.0)
            )
        )
    )

    // Соответствует формуле 10
    printOperationResult(
        Value(10.0)
    )
}

class Multiply(val left: Operation, val right: Operation) : Operation {
    override fun calculate() = left.calculate() * right.calculate()
}

class Value(val value: Double) : Operation {
    override fun calculate() = value
}

class Plus(val left: Operation, val right: Operation) : Operation {
    override fun calculate(): Double = left.calculate() + right.calculate()
}
