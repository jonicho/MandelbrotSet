package de.jrk.mandelbrotset

object MandelbrotSet {
    fun testMandelbrot(zRe: Double, zIm: Double, maxIterations: Int): Int {
        var re = zRe
        var im = zIm
        var i = 0
        do {
            if (re * re + im * im > 4) return i
            val newZre = re * re - im * im + zRe
            im = im * re * 2 + zIm
            re = newZre
        } while (++i < maxIterations)
        return 1
    }
}