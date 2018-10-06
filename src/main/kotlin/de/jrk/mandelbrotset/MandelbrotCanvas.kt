package de.jrk.mandelbrotset

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

class MandelbrotCanvas : JPanel() {
    var cX = -2.0
    var cY = -2.0
    var cWidth = 4.0
    var cHeight = 4.0
    var hueOffset = 0f
    var set = Array(0) { Array(0) {0} }
    var iterations = 10

    override fun paintComponent(g: Graphics) {
        if (set.isEmpty()) {
            generateMandelbrotSet()
        }
        val img = BufferedImage(set.size, set[0].size, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until set.size) {
            for (y in 0 until set[0].size) {
                val hue = if (set[x][y] == -1) 0.0.toFloat() else set[x][y] / iterations.toFloat()
                img.setRGB(x, y, Color.HSBtoRGB(hue + hueOffset, 0.5f, 1f))
            }
        }
        g.drawImage(img, 0, 0, null)
    }

    fun generateMandelbrotSet() {
        set = MandelbrotSet.generateMandelbrotSet(width, height, cX, cY, cWidth, cHeight, iterations)
    }
}