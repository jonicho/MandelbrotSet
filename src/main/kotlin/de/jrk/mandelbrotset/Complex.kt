package de.jrk.mandelbrotset

import kotlin.math.pow
import kotlin.math.sqrt

data class Complex(val re: Double, val im: Double) {
    constructor(d: Double) : this(d, 0.0)

    val absuluteValue get() = sqrt(re.pow(2) + im.pow(2))

    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)

    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)

    operator fun times(other: Complex) = Complex(re * other.re - im * other.im, im * other.re + re * other.im)

    operator fun div(other: Complex) = Complex((re * other.re + im * other.im) / (other.re.pow(2) + other.im.pow(2)), (im * other.re - re * other.im) / (other.re.pow(2) + other.im.pow(2)))

    operator fun unaryMinus() = Complex(-re, -im)
}
