package de.jrk.mandelbrotset

import java.awt.Color

interface MandelbrotColoring {
    fun color(d: Double): Color

    companion object {
        private val colorings: List<MandelbrotColoring> = listOf(
                object : MandelbrotColoring {
                    override fun color(d: Double): Color {
                        return Color.getHSBColor(d.toFloat(), 0.5f, 1f)
                    }
                }
        )

        val allColorings get() = colorings

        fun getColoring(id: Int) = colorings[id]

        fun getId(coloring: MandelbrotColoring) = colorings.indexOf(coloring)
    }
}