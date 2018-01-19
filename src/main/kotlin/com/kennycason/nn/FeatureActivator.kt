package com.kennycason.nn

import org.jblas.DoubleMatrix


/**
 * Stimulate inactive neurons/features
 */
class FeatureActivator(val layer: AutoEncoder,
                       val inativeThreshold: Double = 0.1,
                       val learningRate: Double = 0.005) {

    private val sampleSums = DoubleMatrix(1, layer.encode.columns)

    private var samples = 0

    fun sample(x: DoubleMatrix) {
        sampleSums.addi(layer.encode(x))
        samples++
    }

    fun clear() {
        sampleSums.muli(0.0)
        samples = 0
    }

    // stimulate inactive features
    fun update() {
        val inactive = generateInactivityVector()
        if (inactive.sum() == 0.0) { return }

        // println("stimulate inactivate neurons")
        layer.encode.addiRowVector(inactive.mul(learningRate))
    }

    // build a binary (1,0) vector where inactive features are set to 1
    // use this vector for downstream updates to nn
    fun generateInactivityVector() = sampleSums.apply { i -> if (i < inativeThreshold * samples) { 1.0 } else { 0.0 } }

}