package de.jrk.mandelbrotset

import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel

class MandelbrotCanvas : JPanel() {
    private var cX = -2.0
    private var cY = -2.0
    private var cWidth = 4.0
    private var cHeight = 4.0
    private var set = Array(0) { Array(0) { 0 } }
    var hueOffset = 0f
    var iterations = 10
    var zoomFactor = 2.0
        set(value) {
            if (value > 0) field = value
        }

    init {
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                zoom(e.x, e.y, when (e.button) {
                    MouseEvent.BUTTON1 -> zoomFactor
                    MouseEvent.BUTTON3 -> 1 / zoomFactor
                    else -> 1.0
                })
            }
        })
    }

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

    fun zoom(centerX: Int, centerY: Int, factor: Double) {
        val centerCX = cX + (cWidth * centerX) / width
        val centerCY = cY + (cHeight * centerY) / height
        println("$centerCX, $centerCY, $factor")
        cX = centerCX - cWidth / 2
        cY = centerCY - cHeight / 2
        cX += (cWidth - cWidth / factor) / 2
        cY += (cHeight - cHeight / factor) / 2
        cWidth /= factor
        cHeight /= factor
        generateMandelbrotSet()
        repaint()
    }
}