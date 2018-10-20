package de.jrk.mandelbrotset

import java.awt.Color
import java.awt.image.BufferedImage

object MandelbrotRenderer {
    fun render(set: Array<Array<Double>>): BufferedImage {
        val img = BufferedImage(set.size, set[0].size, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until set.size) {
            for (y in 0 until set[0].size) {
                val hue = set[x][y]
                img.setRGB(x, y, Color.HSBtoRGB((hue).toFloat(), 0.5f, 1f))
            }
        }
        return img
    }
}