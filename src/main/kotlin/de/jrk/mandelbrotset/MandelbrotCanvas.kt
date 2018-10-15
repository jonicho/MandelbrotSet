package de.jrk.mandelbrotset

import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JPanel

class MandelbrotCanvas : JPanel() {
    var cX = -2.0
        private set
    var cY = -2.0
        private set
    var cWidth = 4.0
        private set
    var cHeight = 4.0
        private set
    private val mandelbrotGenerator = MandelbrotGenerator()
    private val set get() = mandelbrotGenerator.set
    var hueOffset = 0f
    var maxIterations = 10
    var zoomFactor = 2.0
        set(value) {
            if (value > 0) field = value
        }
    val zoomListeners = ArrayList<() -> Unit>()

    init {
        addZoomListener {
            generateMandelbrotSet()
            repaint()
        }
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
        val img = BufferedImage(set.size, set[0].size, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until set.size) {
            for (y in 0 until set[0].size) {
                val hue = if (set[x][y] == -1.0) 0.0 else set[x][y]
                img.setRGB(x, y, Color.HSBtoRGB((hue + hueOffset).toFloat(), 0.5f, 1f))
            }
        }
        g.drawImage(img, 0, 0, null)
    }

    fun generateMandelbrotSet() {
        mandelbrotGenerator.generateMandelbrotSet(width, height, cX, cY, cWidth, cHeight, maxIterations)
        Thread {
            while (mandelbrotGenerator.isGenerating) {
                repaint()
                Thread.sleep(10)
            }
            repaint()
        }.start()
    }

    fun zoom(centerX: Int, centerY: Int, factor: Double) {
        val centerCX = cX + (cWidth * centerX) / width
        val centerCY = cY + (cHeight * centerY) / height
        cX = centerCX - cWidth / 2
        cY = centerCY - cHeight / 2
        cX += (cWidth - cWidth / factor) / 2
        cY += (cHeight - cHeight / factor) / 2
        cWidth /= factor
        cHeight /= factor
        zoomListeners.forEach { it() }
    }

    fun addZoomListener(zoomListener: () -> Unit) {
        zoomListeners.add(zoomListener)
    }
}