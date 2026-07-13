package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.EnumOption.Option

val JPEG_QUALITY_OPTIONS = listOf(
    Option("1", "Best (1)"),
    Option("2", "Best (2)"),
    Option("3", "Best (3)"),
    Option("4", "Best (4)"),
    Option("5", "Best (5)"),
    Option("6", "Best (6)"),
    Option("7", "Good (7)"),
    Option("8", "Good (8)"),
    Option("9", "Good (9)"),
    Option("10", "Good (10)"),
    Option("11", "Good (11)"),
    Option("12", "Good (12)"),
    Option("13", "Average (13)"),
    Option("14", "Average (14)"),
    Option("15", "Average (15)"),
    Option("16", "Average (16)"),
    Option("17", "Average (17)"),
    Option("18", "Average (18)"),
    Option("19", "Average (19)"),
    Option("20", "Bad (20)"),
    Option("21", "Bad (21)"),
    Option("22", "Bad (22)"),
    Option("23", "Bad (23)"),
    Option("24", "Bad (24)"),
    Option("25", "Bad (25)"),
    Option("26", "Worst (26)"),
    Option("27", "Worst (27)"),
    Option("28", "Worst (28)"),
    Option("29", "Worst (29)"),
    Option("30", "Worst (30)"),
    Option("31", "Worst (31)"),
)

class JpegQualityOption(defaultValue: Option?) : EnumOption(defaultValue) {
    override val LABEL = "Quality"
    override val KEY = "-q:v"
    override val REQUIRED = true

    override val OPTIONS = JPEG_QUALITY_OPTIONS

    companion object {
        fun init() = JpegQualityOption(JPEG_QUALITY_OPTIONS[4])
    }
}