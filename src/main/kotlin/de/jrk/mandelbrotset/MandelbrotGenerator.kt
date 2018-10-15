package de.jrk.mandelbrotset

import kotlin.math.absoluteValue

class MandelbrotGenerator(val numThreads: Int = 4, val numBatchRows: Int = 16) {
    private var generatorThread: Thread = Thread()
    private var stop = false
    var set = Array(1) { Array(1) { 0 } }
        private set
    var isGenerating = false
        private set

    fun generateMandelbrotSet(width: Int, height: Int, cX: Double, cY: Double, cWidth: Double, cHeight: Double, iterations: Int) = synchronized(this) {
        if (isGenerating) stopGenerating()
        if (set.size != width || set[0].size != height) {
            set = Array(width) { Array(height) { 0 } }
        }

        generatorThread = Thread {
            isGenerating = true
            val spiralBatches = object {
                //see: https://stackoverflow.com/a/1196922
                var x = 0
                var y = 0
                var dx = 0
                var dy = -1
                var max = numBatchRows * numBatchRows
                val halfNumBatchRows = numBatchRows / 2
                var i = 0
                val isFinished get() = synchronized(this) { i >= max }
                fun nextBatch(): Pair<Int, Int> {
                    synchronized(this) {
                        i++
                        if ((x.absoluteValue == y.absoluteValue && (dx != 1 || dy != 0)) || (x > 0 && y == 1 - x)) {
                            val odx = dx
                            dx = -dy
                            dy = odx
                        }
                        if (x.absoluteValue > halfNumBatchRows || y.absoluteValue > halfNumBatchRows) {
                            val odx = dx
                            dx = -dy
                            dy = odx
                            val ox = x
                            x = -y + dx
                            y = ox + dy
                        }
                        val p = Pair(halfNumBatchRows - x, halfNumBatchRows - y)
                        x += dx
                        y += dy
                        return p
                    }
                }
            }
            val threads = Array(numThreads) { i ->
                Thread {
                    while (!spiralBatches.isFinished && !stop) {
                        val (x, y) = spiralBatches.nextBatch()
                        MandelbrotSet.generateMandelbrotSet(set, cX, cY, cWidth, cHeight, iterations,
                                x = Math.round(x * set.size.toDouble() / numBatchRows).toInt(), width = Math.round(set.size.toDouble() / numBatchRows).toInt(),
                                y = Math.round(y * set.size.toDouble() / numBatchRows).toInt(), height = Math.round(set.size.toDouble() / numBatchRows).toInt(),
                                stopWhen = { stop })
                    }
                }.also {
                    it.priority = Thread.MIN_PRIORITY
                    it.start()
                }
            }
            for (thread in threads) {
                thread.join()
            }
            isGenerating = false
        }
        generatorThread.start()
    }

    fun stopGenerating() {
        stop = true
        generatorThread.join()
        stop = false
    }
}