package de.jrk.mandelbrotset

import kotlin.math.absoluteValue

class MandelbrotGenerator(val numThreads: Int = 4, val numBatchRows: Int = 16) {
    private var generatorThread: Thread = Thread()
    private var threads = emptyArray<GeneratorThread>()
    private var stop = false
    var set = Array(1) { Array(1) { 0.0 } }
        private set
    var isGenerating = false
        private set
    private var batchGenerator = BatchGenerator()
    val progress
        get() =
            if (threads.isEmpty()) 1.0 else threads.map { it.batchProgress }.reduce { acc, d -> acc + d } / batchGenerator.max

    fun generateMandelbrotSet(width: Int, height: Int, cX: Double, cY: Double, cWidth: Double, cHeight: Double, maxIterations: Int) = synchronized(this) {
        if (isGenerating) stopGenerating()
        if (set.size != width || set[0].size != height) {
            set = Array(width) { Array(height) { 0.0 } }
        }

        generatorThread = Thread {
            isGenerating = true
            batchGenerator = BatchGenerator()
            threads = Array(numThreads) { _ ->
                GeneratorThread(batchGenerator, cX, cY, cWidth, cHeight, maxIterations).also {
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

    fun stopGenerating() = synchronized(this) {
        stop = true
        generatorThread.join()
        stop = false
    }

    private inner class GeneratorThread(val batchGenerator: BatchGenerator, val cX: Double, val cY: Double, val cWidth: Double, val cHeight: Double, val maxIterations: Int) : Thread() {
        private var completedBatches = 0
        private var currentBatchProgress = 0.0

        val batchProgress get() = completedBatches + currentBatchProgress

        override fun run() {
            while (!batchGenerator.isFinished) {
                val (batchPosX, batchPosY) = batchGenerator.nextBatch()
                val batchX = Math.round(batchPosX * set.size.toDouble() / numBatchRows).toInt()
                val batchY = Math.round(batchPosY * set.size.toDouble() / numBatchRows).toInt()
                val batchWidth = Math.round(set.size.toDouble() / numBatchRows).toInt()
                val batchHeight = Math.round(set.size.toDouble() / numBatchRows).toInt()
                val max = batchWidth * batchHeight
                var i = 0
                for (x in batchX until batchX + batchWidth) {
                    for (y in batchY until batchY + batchHeight) {
                        if (stop) return
                        val c = Complex(
                                (x.toDouble() / set.size) * cWidth + cX,
                                (y.toDouble() / set[0].size) * cHeight + cY
                        )
                        val iterations = MandelbrotSet.testMandelbrot(c, maxIterations)
                        set[x][y] = if (iterations == -1) -1.0 else iterations.toDouble() / maxIterations
                        i++
                        currentBatchProgress = i.toDouble() / max
                    }
                }
                completedBatches++
                currentBatchProgress = 0.0
            }
        }
    }

    private inner class BatchGenerator {
        //see: https://stackoverflow.com/a/1196922
        private var x = 0
        private var y = 0
        private var dx = 0
        private var dy = -1
        val max = numBatchRows * numBatchRows
        private val halfNumBatchRows = numBatchRows / 2
        private var i = 0
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
}