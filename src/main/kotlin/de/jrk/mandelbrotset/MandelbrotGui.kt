package de.jrk.mandelbrotset

import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class MandelbrotGui : JFrame() {
    init {
        setSize(800, 600)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        val panel = JPanel()
        panel.layout = BorderLayout()
        val mandelbrotCanvas = MandelbrotCanvas()
        panel.add(mandelbrotCanvas, BorderLayout.CENTER)

        val controlPanel = JPanel()
        val controlPanelGridBagLayout = GridBagLayout()
        controlPanel.layout = controlPanelGridBagLayout
        panel.add(controlPanel, BorderLayout.WEST)

        val iterationsLabel = JLabel("iterations")
        controlPanelGridBagLayout.setConstraints(iterationsLabel, gridBagConstraints(0, 0))
        controlPanel.add(iterationsLabel)

        val iterationsTextField = JTextField()
        iterationsTextField.document.addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) = update()
            override fun insertUpdate(e: DocumentEvent?) = update()
            override fun removeUpdate(e: DocumentEvent?) = update()

            private fun update() {
                try {
                    mandelbrotCanvas.iterations = iterationsTextField.text.toInt()
                } catch (e: NumberFormatException) {
                }
            }

        })
        controlPanelGridBagLayout.setConstraints(iterationsTextField, gridBagConstraints(1, 0, fill = GridBagConstraints.BOTH))
        controlPanel.add(iterationsTextField)

        val hueOffsetLabel = JLabel("hue offset")
        controlPanelGridBagLayout.setConstraints(hueOffsetLabel, gridBagConstraints(0, 1))
        controlPanel.add(hueOffsetLabel)

        val hueOffsetSlider = JSlider(0, 100, 0)
        hueOffsetSlider.addChangeListener {
            mandelbrotCanvas.hueOffset = hueOffsetSlider.value / 100f
            mandelbrotCanvas.repaint()
        }
        controlPanelGridBagLayout.setConstraints(hueOffsetSlider, gridBagConstraints(1, 1))
        controlPanel.add(hueOffsetSlider)

        val generateButton = JButton("generate set")
        generateButton.addActionListener {
            mandelbrotCanvas.generateMandelbrotSet()
            mandelbrotCanvas.repaint()
        }
        controlPanelGridBagLayout.setConstraints(generateButton, gridBagConstraints(0, 2, 2))
        controlPanel.add(generateButton)

        add(panel)
        isVisible = true
    }


    private fun gridBagConstraints(gridx: Int = GridBagConstraints.RELATIVE,
                                   gridy: Int = GridBagConstraints.RELATIVE,
                                   gridwidth: Int = 1,
                                   gridheight: Int = 1,
                                   weightx: Double = 0.0,
                                   weighty: Double = 0.0,
                                   anchor: Int = GridBagConstraints.CENTER,
                                   fill: Int = GridBagConstraints.NONE,
                                   insets: Insets = Insets(0, 0, 0, 0),
                                   ipadx: Int = 0,
                                   ipady: Int = 0) = GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady)
}