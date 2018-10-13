package de.jrk.mandelbrotset

class MandelbrotGenerator(var numThreads: Int = 4) {
    private val threads = emptyArray<Thread>()
    var set = Array(1) {Array(1) {0} }
        private set
    var isGenerating = false
        private set

    fun generateMandelbrotSet(width: Int, height: Int, cX: Double, cY: Double, cWidth: Double, cHeight: Double, iterations: Int) {
        if (isGenerating) return
        if (set.size != width || set[0].size != height) {
            set = Array(width) { Array(height) { 0 } }
        }

        Thread {
            isGenerating = true
            val threads = Array(numThreads) {i ->
                Thread {
                    MandelbrotSet.generateMandelbrotSet(set, cX, cY, cWidth, cHeight, iterations, x = i * (set.size / numThreads), width = set.size / numThreads)
                }.also { it.start() }
            }
            for(thread in threads) {
                thread.join()
            }
            isGenerating = false
        }.start()
    }
}