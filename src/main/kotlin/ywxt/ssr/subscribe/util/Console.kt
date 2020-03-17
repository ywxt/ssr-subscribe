@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.console

import java.util.*

private val input = Scanner(System.`in`)

// color
inline fun <reified T> T.error(): String = "\u001b[31m${this.toString()}\u001b[0m"
inline fun <reified T> T.warning(): String = "\u001b[33m${this.toString()}\u001b[0m"

/**
 * 输出错误
 */
fun eprintln(message: String) = println("错误：${message}".error())

/**
 * 输出警告
 */
fun wprintln(message: String) = println("警告：${message}".warning())

// ui
fun printGroup(name: String, items: Iterable<String>) {
    println("$name:")
    for (it in items) {
        println("    $it")
    }
}

/**
 * 打印一组
 * 例：
 *
 * [name]:
 *
 *   [items]
 *
 *   [items]
 */
fun printGroup(name: String, items: Sequence<String>) {
    println("$name:")
    for (it in items) {
        println("    $it")
    }
}

fun confirm(tip: String): Boolean {
    while (true) {
        print("$tip (Y/n):")
        val answer = input.next()[0]
        if (answer=='Y' || answer=='y') return true
        else if (answer=='N' || answer=='n') return false
        else eprintln("输入格式不正确：只能为Y、N、y或n")
    }
}