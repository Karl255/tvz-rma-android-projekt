package com.tvz.kbistrick.ffmediatools.model

import com.tvz.kbistrick.ffmediatools.model.MediaFormat.*
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.ChromaSubsamplingOption
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.FFMpegOption
import com.tvz.kbistrick.ffmediatools.model.ffmpegoption.VideoBitrateOption

enum class VideoCodec(
    val displayText: String,
    val code: String,
    val supportsEncode: Boolean,
    val supportedContainers: List<MediaFormat>,
    val optionInits: List<() -> FFMpegOption> = emptyList(),
) {
    H264("H264", "h264", true, listOf(MP4, MKV), listOf(VideoBitrateOption::init, ChromaSubsamplingOption::init)),
    H265("H265", "hevc", true, listOf(MP4, MKV), listOf(VideoBitrateOption::init, ChromaSubsamplingOption::init)),
    H266("H266", "", false, listOf(MP4, MKV), listOf(VideoBitrateOption::init, ChromaSubsamplingOption::init)),
    VP8("VP8", "vp8", true, listOf(MKV, WEBM), listOf(VideoBitrateOption::init, ChromaSubsamplingOption::init)),
    VP9("VP9", "vp9", false, listOf(MKV, WEBM), listOf(VideoBitrateOption::init, ChromaSubsamplingOption::init)),
    AV1("AV1", "av1", false, listOf(MP4, MKV, WEBM), listOf(VideoBitrateOption::init, ChromaSubsamplingOption::init));

    companion object {
        fun allForFormat(container: MediaFormat) = entries.filter { it.supportedContainers.contains(container) }
    }
}