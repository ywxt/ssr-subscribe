@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.console

import ywxt.ssr.subscribe.ssr.SsrUrl
import ywxt.ssr.subscribe.util.prettyName
import java.util.*
import kotlin.collections.HashMap

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
fun printGroup(index: Int, name: String, items: Iterable<SsrUrl>) {
    println("$index $name:")
    for (it in items.withIndex()) {
        println("    ${index}.${it.index} ${it.value.prettyName}")
    }
}

/**
 * 打印一组
 * 例：
 *
 * [name]:
 *
 *     [items]
 *
 *     [items]
 */
fun printGroup(index: Int, name: String, items: Sequence<SsrUrl>) {
    println("$index $name:")
    for (it in items.withIndex()) {
        println("    ${index}-${it.index} ${it.value.prettyName}")
    }
}


fun printGroups(groups: Map<String, Iterable<SsrUrl>>) {
    for (item in groups.entries.withIndex()) {
        printGroup(item.index, item.value.key, item.value.value)
    }
}


fun confirm(tip: String): Boolean {
    while (true) {
        print("$tip (Y/n):")
        val answer = input.next()[0]
        if (answer == 'Y' || answer == 'y') return true
        else if (answer == 'N' || answer == 'n') return false
        else eprintln("输入格式不正确：只能为Y、N、y或n")
    }
}