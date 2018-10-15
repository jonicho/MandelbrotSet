package de.jrk.mandelbrotset

object MandelbrotSet {
    fun testMandelbrot(c: Complex, iterations: Int): Int {
        var z = c
        var i = 0
        do {
            if (z.squaredAbsoluteValue > 4) return i
            z = z * z + c
        } while (++i < iterations)
        return -1
    }
}