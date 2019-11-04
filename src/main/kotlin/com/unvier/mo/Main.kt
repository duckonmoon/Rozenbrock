package com.unvier.mo

import org.la4j.Vector
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {
    val v1 = Vector.fromArray(arrayOf(3.0,1.0).toDoubleArray())
    val v2 = Vector.fromArray(arrayOf(2.0,2.0).toDoubleArray())
    wow(v2, v1)
    ortogonalization2(v1,v2)
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

fun ortogonalization3(u: Vector, v: Vector): Vector {
    val u1 = u.copy()
    val u2 = v.subtract(u1.proj(v))

    print(u1.divide(u1.norm()))
    print(u2.divide(u2.norm()))
    return Vector.fromArray(arrayOf(0.0).toDoubleArray())
}



fun wow(x0: Vector, x: Vector) {
    // процес ортогоналізації грама шмідта
    val l1 = x0[0] - x[0]
    // writer.WriteLine("L1 = " + L1 );
    val l2 = x0[1] - x[1]
    // writer.WriteLine("L2 = " + L2 );

    // ваще хз блять
    val a1 = Vector.fromArray(arrayOf(l1, l2).toDoubleArray())
    val a2 = Vector.fromArray(arrayOf(0.0, l2).toDoubleArray())

    val d1 = a1.divide(norm(a1))

    val b = a2.subtract(d1.proj(a2))


//            Vector.fromArray(arrayOf(
//            a2[0] - (a2[0] * d1[0] + a2[1] * d1[1]) * d1[0],
//            a2[1] - (a2[0] * d1[0] + a2[1] * d1[1]) * d1[1]
//    ).toDoubleArray())



    val d = b.divide(norm(b))
    println(d)
}

private fun norm(vector: Vector): Double {
    var sum = 0.0
    for (i in 0 until vector.length()) {
        sum += vector[i].pow(2)
    }
    return sqrt(sum)
}