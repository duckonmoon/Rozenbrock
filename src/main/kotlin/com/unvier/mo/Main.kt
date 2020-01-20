package com.unvier.mo

import org.la4j.Vector
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val a1 = Vector.fromArray(arrayOf(1.0, 0.0, 1.0, 0.0, 0.0).toDoubleArray())
    val a2 = Vector.fromArray(arrayOf(2.0, 1.0, 0.0, 1.0, 0.0).toDoubleArray())
    val a3 = Vector.fromArray(arrayOf(3.0, 2.0, 1.0, 0.0, 1.0).toDoubleArray())
    val (d1, d2, d3) = ortogonalization3(a1, a2, a3)
    println(d1)
    println(d2)
    println(d3)
}

// proj_u(v)
fun Vector.proj(v: Vector): Vector {
    return this.multiply(this.innerProduct(v) / this.innerProduct(this))
}


fun ortogonalization2(u: Vector, v: Vector): Vector {
    val u1 = u.copy()
    val u2 = v.subtract(u1.proj(v))

    println(u1.divide(u1.norm()))
    println(u2.divide(u2.norm()))
    return Vector.fromArray(arrayOf(0.0).toDoubleArray())
}

fun ortogonalization3(u: Vector, v: Vector, k: Vector): List<Vector> {
    val u1 = u.copy()
    val u2 = v.subtract(u1.proj(v))
    val u3 = k.subtract(u1.proj(k)).subtract(u2.proj(k))
    return listOf(
            u1.divide(u1.norm()),
            u2.divide(u2.norm()),
            u3.divide(u3.norm())
    )
}


fun wow(a1: Vector, a2: Vector) {

    val d1 = a1.divide(norm(a1))

    val b = a2.subtract(d1.proj(a2))

    val d = b.divide(b.norm())
    println(d)
}

private fun norm(vector: Vector): Double {
    var sum = 0.0
    for (i in 0 until vector.length()) {
        sum += vector[i].pow(2)
    }
    return sqrt(sum)
}