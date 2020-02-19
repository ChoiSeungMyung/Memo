package com.programmers.android.apps.line.extensions

import android.util.Log

fun tag(): String {
    val trace = Thread.currentThread().stackTrace[4]
    val fileName = trace.fileName
    val classPath = trace.className
    val className = classPath.substring(classPath.lastIndexOf(".") + 1)
    val methodName = trace.methodName
    val lineNumber = trace.lineNumber
    return "$className.$methodName($fileName:$lineNumber)"
}
fun logv(msg: String) {
    Log.v(tag(), msg)
}

fun logd(msg: String) {
    Log.d(tag(), msg)
}

fun logi(msg: String) {
    Log.i(tag(), msg)
}

fun logw(msg: String) {
    Log.w(tag(), msg)
}

fun loge(msg: String) {
    Log.e(tag(), msg)
}