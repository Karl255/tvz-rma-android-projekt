package com.tvz.kbistrick.ffmediatools.model

import com.tvz.kbistrick.ffmediatools.model.MediaFormat.*

enum class VideoCodec(val displayText: String, supportedContainers: List<MediaFormat>) {
    H264("H264", listOf(MP4, MKV)),
    H265("H265", listOf(MP4, MKV)),
    H266("H266", listOf(MP4, MKV)),
    VP8("VP8", listOf(MKV, WEBM)),
    VP9("VP9", listOf(MKV, WEBM)),
    AV1("AV1", listOf(MP4, MKV, WEBM)),
}