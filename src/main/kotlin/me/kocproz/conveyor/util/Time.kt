package me.kocproz.conveyor.util

inline fun <T> measureTimeMillisAndPrintTime(msg: String = "Execution", block: () -> T): T {
    val start = System.currentTimeMillis()
    val result = block()
    println("$msg time: ${System.currentTimeMillis() - start}")
    return result
}
