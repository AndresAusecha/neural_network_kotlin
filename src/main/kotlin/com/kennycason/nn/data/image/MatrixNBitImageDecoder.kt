package com.kennycason.nn.data.image

import org.jblas.FloatMatrix
import java.awt.image.BufferedImage

// DO NOT USE UNTIL MATRIX DATA IS RE-ORIENTED TO ROW BASED FORM
class MatrixNBitImageDecoder(private val bits: Int = 24,
                             private val threshold: Double = 0.5,
                             rows: Int) : MatrixImageDecoder(rows) {

    private val RGB_BITS = 24

    private val HIGH_BIT_FLAG = 8388608

    override fun decode(data: FloatMatrix): Image {
        val cols = data.columns / bits / rows
        val bi = BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB)
        var y = 0
        for (x in 0 until cols * rows) {
            read(data, bi, x, y)
            if (x > 0 && x % cols == 0) {
                y++
            }
        }
        return Image(bi)
    }

    private fun read(data: FloatMatrix, bi: BufferedImage, x: Int, y: Int) {
        val cols = data.columns / bits / rows
        var rgb = 0
        var offset = 0
        var flag = HIGH_BIT_FLAG
        while (flag > 0) {
            val set = data.get(0, x * bits + offset) > threshold
            if (set) {
                rgb += flag
            }
            offset++
            flag = flag shr RGB_BITS / bits
        }
        bi.setRGB(x % cols, y, rgb)
    }

}