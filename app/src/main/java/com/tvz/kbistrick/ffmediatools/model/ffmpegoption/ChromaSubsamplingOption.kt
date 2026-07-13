package com.tvz.kbistrick.ffmediatools.model.ffmpegoption

class ChromaSubsamplingOption(defaultValue: Option?) : EnumOption(defaultValue) {
    override val LABEL = "Chroma subsampling"
    override val KEY = "-vf"
    override val REQUIRED = false

    override val OPTIONS = listOf(
        Option("format=yuv444p", "8-bit 4:4:4 (no subsamppling)"),
        Option("format=yuv422p", "8-bit 4:2:2"),
        Option("format=yuv420p", "8-bit 4:2:0"),
        Option("format=yuv444p10le", "10-bit 4:4:4 (no subsamppling)"),
        Option("format=yuv422p10le", "10-bit 4:2:2"),
        Option("format=yuv420p10le", "10-bit 4:2:0"),
    )

    companion object {
        fun init() = ChromaSubsamplingOption(null)
    }
}