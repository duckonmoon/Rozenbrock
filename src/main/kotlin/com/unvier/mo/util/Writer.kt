package com.unvier.mo.util

class Writer {
    private val stringBuilder: StringBuilder = StringBuilder()

    fun writeLine(str: String) {
        stringBuilder.append("$str\n")
    }

    fun write(str: String) {
        stringBuilder.append(str)
    }

    fun error() {
        stringBuilder.clear()
        stringBuilder.append("Critical Error")
    }

    override fun toString(): String {
        return stringBuilder.toString()
    }


}