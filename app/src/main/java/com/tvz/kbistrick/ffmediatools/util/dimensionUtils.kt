package com.tvz.kbistrick.ffmediatools.util

import com.tvz.kbistrick.ffmediatools.model.DimensionUnit
import com.tvz.kbistrick.ffmediatools.model.DimensionValue

/**
 *  @param pixelsAt100Percent If provided, unit conversions will be dimensionally stable.
 */
fun DimensionValue.toggleUnit(pixelsAt100Percent: Int?): DimensionValue {
    return when (this.unit) {
        DimensionUnit.PIXEL -> DimensionValue(
            if (pixelsAt100Percent != null) this.number * 100 / pixelsAt100Percent
            else this.number,
            DimensionUnit.PERCENT
        )

        DimensionUnit.PERCENT -> DimensionValue(
            if (pixelsAt100Percent != null) this.number * pixelsAt100Percent / 100
            else this.number,
            DimensionUnit.PIXEL
        )
    }
}

fun Pair<Int, Int>.toAspectRatio() = first.toFloat() / second
