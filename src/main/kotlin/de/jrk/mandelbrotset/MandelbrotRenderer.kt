package de.jrk.mandelbrotset

import java.awt.image.BufferedImage

object MandelbrotRenderer {
    fun render(set: Array<Array<Double>>, coloring: MandelbrotColoring): BufferedImage {
        val img = BufferedImage(set.size, set[0].size, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until set.size) {
            for (y in 0 until set[0].size) {
                img.setRGB(x, y, coloring.color(set[x][y]).rgb)
            }
        }
        return img
    }
}