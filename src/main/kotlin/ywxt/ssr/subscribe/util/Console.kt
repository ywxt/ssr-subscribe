@file:Suppress("PackageDirectoryMismatch")

package ywxt.ssr.subscribe.util.console

import ywxt.ssr.subscribe.config.ServerConfig
import ywxt.ssr.subscribe.util.config.prettyName
import java.util.*

private val input = Scanner(System.`in`)

// color
private inline fun <reified T> T.error(): String = "\u001b[31m${this.toString()}\u001b[0m"
private inline fun <reified T> T.warning(): String = "\u001b[33m${this.toString()}\u001b[0m"
private inline fun <reified T> T.success(): String = "\u001b[32m${this.toString()}\u001b[0m"

/**
 * 输出错误
 */
fun eprintln(message: String) = println("错误：${message}".error())

/**
 * 输出警告
 */
fun wprintln(message: String) = println("警告：${message}".warning())

/**
 * 输出成功
 */
fun sprintln(message: String) = println("成功：${message}".success())

// ui
fun printGroup(index: Int, name: String, items: Iterable<ServerConfig>) {
    println("$index $name:")
    for (it in items.withIndex()) {
        println("    ${index}-${it.index} ${it.value.prettyName}")
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
fun printGroup(index: Int, name: String, items: Sequence<ServerConfig>) {
    println("$index $name:")
    for (it in items.withIndex()) {
        println("    ${index}-${it.index} ${it.value.prettyName}")
    }
}


fun printGroups(groups: Map<String, Iterable<ServerConfig>>) {
    for (item in groups.entries.withIndex()) {
        printGroup(item.index, item.value.key, item.value.value)
    }
}

fun printSources(sources: Iterable<String>) {
    for (source in sources.withIndex()) {
        println("${source.index} ${source.value}")
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

