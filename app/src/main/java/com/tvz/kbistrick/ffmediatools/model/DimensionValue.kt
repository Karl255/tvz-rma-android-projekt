package com.tvz.kbistrick.ffmediatools.model

data class DimensionValue(val number: Int, val unit: DimensionUnit) {
    fun toPixels(pixelsAt100Percent: Int?): Int {
        return when (unit) {
            DimensionUnit.PIXEL -> number
            DimensionUnit.PERCENT -> {
                pixelsAt100Percent ?: throw Exception("Unable to convert percentage to pixel size, please use pixels directly")
                number * pixelsAt100Percent / 100
            }
        }
    }
}