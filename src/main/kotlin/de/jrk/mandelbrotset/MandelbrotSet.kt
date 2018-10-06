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

    fun generateMandelbrotSet(width: Int, height: Int, cX: Double, cY: Double, cWidth: Double, cHeight: Double, iterations: Int): Array<Array<Int>> {
        val set = Array(width) {Array(height) {0} }
        for (x in 0 until width) {
            for (y in 0 until height) {
                val c = Complex(
                        (x.toDouble() / width) * cWidth + cX,
                        (y.toDouble() / height) * cHeight + cY
                )
                set[x][y] = testMandelbrot(c, iterations)
            }
        }
        return set
    }
}