@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.console

// color
inline fun <reified T> T.error(): String = "\u001b[31m${this.toString()}\u001b[0m"
inline fun <reified T> T.warning(): String = "\u001b[33m${this.toString()}\u001b[0m"
fun eprintln(message: String) = println("错误：${message}".error())
fun wprintln(message: String) = println("警告：${message}".warning())

// ui
fun printGroup(name: String, items: Iterable<String>) {
    println("$name:")
    items.forEach {
        println("    $it")
    }
}