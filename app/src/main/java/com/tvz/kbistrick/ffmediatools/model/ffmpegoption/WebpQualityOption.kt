package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

class WebpQualityOption(defaultValue: Int?) : RangeOption(defaultValue) {
    override val LABEL: String = "Quality"
    override val KEY: String = "-quality"
    override val MIN: Int = 1
    override val MAX: Int = 100
    override val REQUIRED: Boolean = true

    companion object {
        fun init() = WebpQualityOption(75)
    }
}
