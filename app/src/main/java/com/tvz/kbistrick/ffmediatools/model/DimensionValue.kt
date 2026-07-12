package com.tvz.kbistrick.ffmediatools.model

data class DimensionValue(val number: Int, val unit: DimensionUnit)

data class NullableDimensionValue(val number: Int?, val unit: DimensionUnit) {
    fun coalesce() =
        if (number == null) null
        else DimensionValue(number, unit)
}
