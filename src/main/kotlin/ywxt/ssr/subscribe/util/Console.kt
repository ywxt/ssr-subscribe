package ywxt.ssr.subscribe.util

fun <T> T.error(): String = "\u001b[31m${this.toString()}\u001b[0m"
fun <T> T.warning():String = "\u001b[33m${this.toString()}\u001b[0m"
fun eprintln(message:String) = println("错误：${message}".error())
fun wprintln(message: String) = println("警告：${message}".warning())
