package com.tvz.kbistrick.ffmediatools.model

data class DimensionValue(val number: Int, val unit: DimensionUnit) {
    fun toPixels(pixelsAt100Percent: Int): Int {
        return when (unit) {
            DimensionUnit.PIXEL -> number
            DimensionUnit.PERCENT -> number * pixelsAt100Percent / 100
        }
    }
}