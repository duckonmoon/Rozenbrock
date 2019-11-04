package com.unvier.mo.util

import org.la4j.Matrix
import org.la4j.Vector
import java.util.stream.IntStream


object SecondTask {

    private fun f(x: Vector) = Vector.fromArray(arrayOf(
            x[0] - (2 * x[1]) + x[2] - 4,
            2 * x[0] + x[1] + x[2] - 1,
            x[0] - x[1] - x[2] - 2,
            2 * x[0] + 2 * x[1] + x[2] - 4,
            -x[0] + x[1] - 2 * x[2] - 2
    ).toDoubleArray()
    )

    private fun grad(index: Int): Vector {
        val coefficientsInGradient = arrayOf(
                arrayOf(1.0, -2.0, 1.0).toDoubleArray(),
                arrayOf(2.0, 1.0, 1.0).toDoubleArray(),
                arrayOf(1.0, -1.0, -1.0).toDoubleArray(),
                arrayOf(2.0, 2.0, 1.0).toDoubleArray(),
                arrayOf(-1.0, 1.0, -2.0).toDoubleArray()
        )
        val grad = Matrix.from2DArray(coefficientsInGradient)
        return grad.getRow(index)
    }

    private fun Vector.membersString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in 0 until this.length()) {
            stringBuilder.append("${this[i]};")
        }
        stringBuilder.append(")")
        return stringBuilder.toString()
    }

    fun calculate(x0Input: Array<Double>, eps: Double): String {
        val stringBuilder = StringBuilder()
        var alpha = 1.0
        var x0: Vector
        var xN = Vector.fromArray(x0Input.toDoubleArray())
        var step = 0
        do {
            x0 = xN.copy()
            stringBuilder.append("<============ ${step + 1} ===============>\n")
            stringBuilder.append("X${step++}=${x0.membersString()}\n")

            // знаходимо рівняння з максимальним значенням по модулю
            val funs = f(x0)
            val maxIndex: Int = IntStream.range(0, funs.length())
                    .reduce { a, b -> if (funs[a] < funs[b]) b else a }.orElse(0)
            stringBuilder.append("№ рівняння ${(maxIndex + 1)}\n")

            // беремо рівняння з цим індексом
            var delta = grad(maxIndex).copy()
            stringBuilder.append("Max Delta ${delta.membersString()}\n")

            // якщо без модуля значення рівняння менше 0 то множимо на -1
            stringBuilder.append("F ${(maxIndex + 1)} (x)= ${f(x0)[maxIndex]}\n")
            if (f(x0)[maxIndex] < 0) {
                delta = delta.multiply(-1.0); }
            stringBuilder.append("new Grad ${delta.membersString()}\n")

            // знаходимо h = grad/||grad||
            val h = delta.divide(delta.norm())
            stringBuilder.append("H ${h.membersString()}\n")

            xN = x0.subtract(h.multiply(alpha))
            stringBuilder.append("X $step = + ${xN.membersString()}\n")
            stringBuilder.append("F(X$step)= ${f(xN).membersString()}\n")
            stringBuilder.append("Alpha $alpha\n")

            alpha = alpha * 4 / 5
        } while (f(xN).subtract(f(x0)).norm() > eps)
        stringBuilder.append("<===========================>\n")
        stringBuilder.append("Result:")
        stringBuilder.append("X* = ${xN.membersString()}")
        stringBuilder.append("f(X*) = ${f(xN).membersString()}")
        return stringBuilder.toString()
    }
}