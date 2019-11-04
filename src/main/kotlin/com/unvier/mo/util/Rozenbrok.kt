package com.unvier.mo.util

import org.la4j.Vector
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Rozenbrok(val alfa: Double,
                val beta: Double,
                d1_input: Array<Double>,
                d2_input: Array<Double>,
                delta1: Double,
                delta2: Double,
                val eps: Double,
                x_0: Array<Double>) {
    private var n = 2
    private var step = 1
    private var i = 0
    private val writer = Writer()

    private val delta: MutableList<Double> = mutableListOf(delta1, delta2)
    private var d1: Vector = Vector.fromArray(d1_input.toDoubleArray())
    private var d2: Vector = Vector.fromArray(d2_input.toDoubleArray())

    private var x: Vector = Vector.fromArray(x_0.toDoubleArray())
    private var y: Vector = Vector.fromArray(x_0.toDoubleArray())
    private var z: Vector = Vector.fromArray(x_0.toDoubleArray())
    private var y1: Vector = Vector.fromArray(x_0.toDoubleArray())
    private var x0: Vector = Vector.fromArray(x_0.toDoubleArray())

    companion object {
        private fun function(x: Vector): Double = 2 * sqrt(x[0].pow(2) + 2 * x[1].pow(2) + 3) - x[0]
        //x[0].pow(4.0) + 2 * x[1].pow(4.0) + x[0].pow(2.0) * x[1].pow(2.0) + 2 * x[0] + x[1]
    }

    fun calculate(): String {
        var state = 2
        loop@ while (state != 0) {
            when (state) {
                1 -> {
                    writer.error()
                    break@loop
                }
                2 -> state = two()
                3 -> state = three()
                4 -> state = four()
            }
        }
        return writer.toString()
    }

    private fun two(): Int {
        if (i == 0) {
            writer.writeLine("<============ ${step++} ===============>")
            writer.writeLine("I =$i")  //first
            writer.writeLine(" - Step 2 - ")
            if (function(y.add(d1.multiply(delta[0]))) < function(y)) {
                writer.writeLine("Successful step")
                writer.write("f(Y + del_1*d_1) < f(Y)  ")
                writer.writeLine("${function(y.add(d1.multiply(delta[0])))} < ${function(y)}")

                y = y.add(d1.multiply(delta[0]))

                writer.writeLine("new Y(${y[0]}; ${y[1]})")

                delta[0] = alfa * delta[0]
                writer.writeLine("del_1=${delta[0]}")
                return 3
            } else {
                writer.writeLine("Unsuccessful step")
                writer.write(" f(Y + del_1*d_1) > f(Y)  ")
                writer.writeLine("${function(y.add(d1.multiply(delta[0])))} > ${function(y)}")
                writer.writeLine("new Y(${y[0]}; ${y[1]})")

                delta[0] = beta * delta[0]
                writer.writeLine("del_1=${delta[0]}")
                return 3
            }
        }
        // для другої координати
        if (i == 1) {
            writer.writeLine("I =$i")
            if (function(y.add(d2.multiply(delta[1]))) < function(y)) {
                writer.writeLine("Successful step")
                writer.write(" f(Y + del_2*d_2) < f(Y)  ")
                writer.writeLine("${function(y.add(d2.multiply(delta[1])))} < ${function(y)}")

                y = y.add(d2.multiply(delta[1]))
                writer.writeLine("new Y(${y[0]}; ${y[1]})")

                delta[1] = alfa * delta[1]
                writer.writeLine("del_2=${delta[1]}")
                return 3
            } else {
                writer.writeLine("Unuccessful step")
                writer.write("f(Y + del_2*d_2) > f(Y)  ")
                writer.writeLine("${function(y.add(d2.multiply(delta[1])))} > ${function(y)}")
                writer.writeLine("new Y(${y[0]}; ${y[1]})")

                delta[1] = beta * delta[1]
                writer.writeLine("del_2=${delta[1]}")
                return 3
            }
        }

        return 3
    }


    private fun three(): Int {
        if (i < n) {
            i += 1  // переходимо до наступного елемента вектора
            return 2
        }

        if (i == n) {
            writer.writeLine(" - Step 3 -  I = n ")
            if (function(y) < function(y1)) {
                writer.writeLine(" f(Y) < f(Y1)")
                writer.writeLine("f(Y)= ${function(y)}")
                writer.writeLine("f(Y1)= ${function(y1)}")

                y1 = y.copy()
                writer.writeLine("Yn(${y1[0]}; ${y1[1]})")
                i = 0
                return 2
            } else if (function(y) == function(y1)) {
                writer.write(" f(Y) = f(Y1)  ")
                writer.writeLine("${function(y)} = ${function(y1)}")
                if (function(y1) == function(x)) {
                    writer.writeLine(" f(Y1) == f(X)")
                    y1 = y.copy()
                    writer.writeLine("Yn(${y1[0]}; ${y1[1]})")
                    i = 0
                    return 2
                }
                if (function(y) < function(x)) {
                    writer.writeLine("f(Y) < f(X)")
                    writer.writeLine("${function(y)} < ${function(x)}")
                    return 4
                }
                if (function(y) == function(x)) {
                    writer.writeLine(" f(Y) == f(X)")
                    //if (delta.PointwiseAbs().ForAll(p => p <= Eps))
                    if (delta.stream().map { el -> abs(el) }.allMatch { el -> el < eps }) {
                        writer.writeLine(" Del_1 && Del_2 <= E")
                        writer.writeLine("|del_1|=${abs(delta[0])}")
                        writer.writeLine("|del_2|=${abs(delta[1])}")

                        z = x.copy()
                        writer.writeLine("Z(${z[0]}; ${z[1]})")
                        return 4
                    } else {
                        writer.writeLine("Del_1 || Del_2 > E")
                        writer.writeLine("|del_1|=${abs(delta[0])}")
                        writer.writeLine("|del_2|=${abs(delta[1])}")

                        y1 = y.copy()
                        writer.writeLine("Yn(${y1[0]}; ${y1[1]})")
                        i = 0
                        return 2
                    }

                }
            }
        }
        return 1
    }

    private fun four(): Int {
        x0[0] = y[0]
        x0[1] = y[1]
        writer.writeLine("X0( ${x0[0]} ; ${x0[1]})")
        writer.writeLine("<===========================>")
        //if ((X0 - X).Norm(2) <= Eps)
        if (norm(x0.subtract(x)) <= eps) {
            z[0] = x0[0]
            z[1] = x0[1]
            writer.writeLine("||X0 - X|| = ${norm(x0.subtract(x))} <= Eps")
        } else {
            writer.writeLine("||X0 - X|| = ${norm(x0.subtract(x))} > Eps")
            // процес ортогоналізації грама шмідта
            val l1 = x0[0] - x[0]
            // writer.WriteLine("L1 = " + L1 );
            val l2 = x0[1] - x[1]
            // writer.WriteLine("L2 = " + L2 );

            val a1 = Vector.fromArray(arrayOf(l1, l2).toDoubleArray())
            val a2 = Vector.fromArray(arrayOf(0.0, l2).toDoubleArray())

            d1 = a1.divide(norm(a1))

            val b = Vector.fromArray(arrayOf(
                    a2[0] - (a2[0] * d1[0] + a2[1] * d1[1]) * d1[0],
                    a2[1] - (a2[0] * d1[0] + a2[1] * d1[1]) * d1[1]
            ).toDoubleArray())

            d2[0] = b[0] / norm(b)
            d2[1] = b[1] / norm(b)

            writer.writeLine("d1(${d1[0]}; ${d1[1]})")
            writer.writeLine("d2(${d2[0]}; ${d2[1]})")

            x = y.copy()

            i = 0
            return 2
        }
        writer.writeLine("<===========================>")
        writer.writeLine("Result:")
        writer.writeLine("X* = (${z[0]},${z[1]})")
        writer.writeLine("f(X*) = ${function(z)}")
        return 0
    }


    private fun norm(vector: Vector): Double {
        var sum = 0.0
        for (i in 0 until vector.length()) {
            sum += vector[i].pow(2)
        }
        return sqrt(sum)
    }
}
