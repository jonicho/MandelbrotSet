package de.jrk.mandelbrotset

object MandelbrotSet {
    fun testMandelbrot(c: Complex, maxIterations: Int): Int {
        var z = c
        var i = 0
        do {
            if (z.squaredAbsoluteValue > 4) return i
            z = z * z + c
        } while (++i < maxIterations)
        return -1
    }
}